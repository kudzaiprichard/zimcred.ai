package com.intela.zimcredai.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intela.zimcredai.Models.*;
import com.intela.zimcredai.Repositories.*;
import com.intela.zimcredai.RequestResponseModels.*;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.intela.zimcredai.Util.Util.getUserByToken;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final CoordinatorRepository coordinatorRepository;
    private final CustomerRepository customerRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final PendingUserChangeRepository pendingUserChangeRepository;

    private void saveUserToken(User user, String jwtToken, TokenType type) {
        var token = Token
                .builder()
                .user(user)
                .token(jwtToken)
                .tokenType(type)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token ->{
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var userEmail = userRepository.findByEmail(request.getEmail());
        if(userEmail.isPresent()){throw new RuntimeException("User email already exists");}

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);
        saveUserToken(savedUser, jwtToken, TokenType.ACCESS);
        saveUserToken(savedUser, refreshToken, TokenType.REFRESH);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(), savedUser.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
 
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .createdAt(savedUser.getCreatedAt())
                .build();
    }

    public LoggedUserResponse fetchLoggedInUserByToken(
            HttpServletRequest request
    ){
        User user = getUserByToken(request, jwtService, this.userRepository);
        return LoggedUserResponse.builder()
                .isAuthenticated(Boolean.TRUE)
                .role(user.getRole())
                .email(user.getEmail())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticateRequest request){

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new EntityNotFoundException("User email does not exist"));

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            revokeAllUserTokens(user);
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(user, jwtToken, TokenType.ACCESS);
            saveUserToken(user, refreshToken, TokenType.REFRESH);

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new RuntimeException("Incorrect password");
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        refreshToken = authHeader.split(" ")[1].trim();
        userEmail = jwtService.extractUsername(refreshToken);

        if(userEmail != null){
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Please enter valid token"));

            var isTokenValid = tokenRepository.findByToken(refreshToken)
                    .map(token -> !token.getRevoked() && !token.getExpired() && token.getTokenType().equals(TokenType.REFRESH))
                    .orElse(false);

            if(jwtService.isTokenValid(refreshToken, user) && isTokenValid){
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken, TokenType.ACCESS);
                saveUserToken(user, refreshToken, TokenType.REFRESH);

                var authResponse = AuthenticationResponse
                        .builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
            }

            throw new RuntimeException("Please enter valid refresh token");
        }
    }

    public Boolean isAuthenticated(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Boolean.FALSE;
        }

        String jwtToken = authHeader.split(" ")[1].trim();
        String userEmail = this.jwtService.extractUsername(jwtToken);

        Optional<User> userOptional = this.userRepository.findByEmail(userEmail);

        // Check if the user exists and token is valid
        if (userOptional.isPresent() && this.jwtService.isTokenValid(jwtToken, userOptional.get())) {

            Optional<Token> tokenOptional = tokenRepository.findByToken(jwtToken);

            if (tokenOptional.isPresent()) {
                Token token = tokenOptional.get();
                if (!token.getRevoked() && !token.getExpired()) {
                    return Boolean.TRUE; // Token is valid
                }
            }
        }

        return Boolean.FALSE; // Token is either revoked, expired, or not found
    }

    // Todo : should this methods be here
    public Coordinator fetchCoordinatorDetails(String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Coordinator not found with email : " + email));

        return coordinatorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Coordinator not found with email : " + email));
    }

    // Todo : should this methods be here
    public Customer fetchCustomerDetails(String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with email : " + email));

        return customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with email:" + email));
    }

    // Can also be used to rest password
    @Transactional
    public Boolean updateCredentials(UpdateCredentials credentials, HttpServletRequest httpRequest) throws MessagingException, IOException {
        User user = null;
        boolean isLoggedIn = false;

        // Try to get the logged-in user
        try {
            user = getUserByToken(httpRequest, jwtService, this.userRepository);
            isLoggedIn = true;
        } catch (Exception e) {
            // User is not logged in; handle as a password reset request
        }

        if (!isLoggedIn) {
            // Handle password reset for non-logged-in users
            if (credentials.getPassword() != null && !credentials.getPassword().isEmpty()) {
                if (credentials.getPassword().equals(credentials.getConfirmPassword())) {
                    // Validate the email provided for password reset
                    User userForPasswordReset = userRepository.findByEmail(credentials.getEmail())
                            .orElseThrow(() -> new RuntimeException("Email not found"));

                    // Mark all existing pending changes as expired
                    List<PendingUserChange> existingRequests = pendingUserChangeRepository.findByUserId(userForPasswordReset.getId());
                    existingRequests.forEach(request -> request.setIsExpired(true));
                    pendingUserChangeRepository.saveAll(existingRequests);

                    // Create a new pending change record
                    PendingUserChange pendingUserChange = pendingUserChangeRepository.save(PendingUserChange.builder()
                            .user(userForPasswordReset)
                            .pendingPassword(passwordEncoder.encode(credentials.getPassword()))
                            .isExpired(false)
                            .tokenExpiry(LocalDateTime.now().plusHours(1))
                            .token(jwtService.generateResetToken(userForPasswordReset))
                            .build());

                    // Generate a password reset token
                    String resetLink = httpRequest.getRequestURL().toString().replace(
                            "/updateCredentialsRequest", "/updateCredentials"
                    ) + "?token=" + pendingUserChange.getToken();

                    // Save the reset token in the database
                    saveUserToken(userForPasswordReset, pendingUserChange.getToken(), TokenType.RESET);

                    // Send the password reset email
                    emailService.sendPasswordResetEmail(userForPasswordReset.getEmail(),userForPasswordReset.getEmail(), resetLink);
                    return Boolean.TRUE;
                } else {
                    throw new RuntimeException("Passwords do not match");
                }
            } else {
                throw new RuntimeException("Password must be provided for password reset");
            }
        }

        // Continue with the rest of the updateCredentials logic if logged in
        boolean emailChanged = false;
        boolean passwordChanged = false;

        if (isLoggedIn){
            // Check if the email is being changed
            if (credentials.getEmail() != null && !credentials.getEmail().isEmpty() && !credentials.getEmail().equals(user.getEmail())) {
                user.setEmail(credentials.getEmail());
                user.setIsAccountVerified(Boolean.FALSE); // Unverify account if the email is changed
                emailChanged = true;
            }

            // Check if the password is being changed
            if (credentials.getPassword() != null && !credentials.getPassword().isEmpty()) {
                if (credentials.getPassword().equals(credentials.getConfirmPassword())) {
                    user.setPassword(passwordEncoder.encode(credentials.getPassword()));
                    passwordChanged = true;
                } else {
                    throw new RuntimeException("Passwords do not match");
                }
            }

            // If either the email or password is changed, send a reset token via email
            if (emailChanged || passwordChanged) {
                // Mark all existing pending changes as expired
                List<PendingUserChange> existingRequests = pendingUserChangeRepository.findByUserId(user.getId());
                existingRequests.forEach(request -> request.setIsExpired(true));
                pendingUserChangeRepository.saveAll(existingRequests);

                // Create a new pending change record
                PendingUserChange pendingChange = pendingUserChangeRepository.save(
                        PendingUserChange.builder()
                                .user(user)
                                .pendingEmail(emailChanged ? credentials.getEmail() : null)
                                .pendingPassword(passwordChanged ? passwordEncoder.encode(credentials.getPassword()) : null)
                                .token(jwtService.generateResetToken(user))
                                .tokenExpiry(LocalDateTime.now().plusHours(1))
                                .isExpired(false)
                                .build()
                );

                String resetLink = httpRequest.getRequestURL().toString().replace("/updateCredentialsRequest", "/updateCredentials") + "?token=" + pendingChange.getToken();

                // Determine the name to use in the email
                String name;
                if (user.getRole() == Role.COORDINATOR) {
                    User finalUser = user;
                    Coordinator coordinator = coordinatorRepository.findByUserId(user.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Coordinator not found with email : " + finalUser.getEmail()));

                    name = coordinator.getFirstName() + " " + coordinator.getLastName();
                } else if (user.getRole() == Role.CUSTOMER) {
                    User finalUser1 = user;
                    Customer customer = customerRepository.findByUserId(user.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Customer not found with email: " + finalUser1.getEmail()));
                    name = customer.getFirstName() + " " + customer.getLastName();
                } else {
                    name = user.getEmail(); // Default to email if name is not available
                }

                // Send the credentials reset email
                emailService.sendPasswordResetEmail(name, user.getEmail(), resetLink);
            }
        }
        return Boolean.TRUE;

    }


    @Transactional
    public Boolean confirmCredentialsUpdate(String token) {
        // Find the token in the database
        Optional<PendingUserChange> pendingChangeOptional = pendingUserChangeRepository.findByToken(token);

        if (pendingChangeOptional.isPresent()) {
            PendingUserChange pendingChange = pendingChangeOptional.get();

            // Check if the token is expired or invalid
            if (pendingChange.getIsExpired() || pendingChange.getTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Invalid or expired token.");
            }

            User user = pendingChange.getUser();

            // Apply the pending email and password changes
            if (pendingChange.getPendingEmail() != null) {
                user.setEmail(pendingChange.getPendingEmail());
                user.setIsAccountVerified(Boolean.FALSE); // Unverify the account since the email changed
            }

            if (pendingChange.getPendingPassword() != null) {
                user.setPassword(pendingChange.getPendingPassword());
            }

            userRepository.save(user);

            // Mark all other pending changes for this user as expired
            List<PendingUserChange> allPendingChanges = pendingUserChangeRepository.findByUserId(user.getId());
            allPendingChanges.forEach(change -> change.setIsExpired(true));
            pendingUserChangeRepository.saveAll(allPendingChanges);

            // Expire the token after successful verification
            pendingChange.setIsExpired(true);
            pendingUserChangeRepository.save(pendingChange);

            return Boolean.TRUE;
        }

        throw new RuntimeException("Invalid or expired token.");
    }




    // Todo: add validation e.g like user must enter something to confirm account deletion
    public void deleteAccount(HttpServletRequest request) {
        User user = getUserByToken(request, jwtService, this.userRepository);
        userRepository.delete(user);
    }

    public void deactivateAccount(HttpServletRequest request) {
        User user = getUserByToken(request, jwtService, this.userRepository);
        user.setIsEnabled(Boolean.FALSE);
        userRepository.save(user);
    }

    public void activateAccount(HttpServletRequest request) {
        User user = getUserByToken(request, jwtService, this.userRepository);
        user.setIsEnabled(Boolean.TRUE);
        userRepository.save(user);
    }

    @Transactional
    public Boolean confirmEmail(HttpServletRequest httpServletRequest, String token) {
        // Extract the username from the token
        String userEmail = jwtService.extractUsername(token);

        // Find the user by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        // Find the token in the database
        Optional<Token> storedToken = tokenRepository.findByToken(token);

        if (storedToken.isPresent()) {
            Token tokenEntity = storedToken.get();

            // Validate the token type and ensure it hasn't expired or been revoked
            if (tokenEntity.getTokenType() == TokenType.RESET &&
                    !tokenEntity.getExpired() &&
                    !tokenEntity.getRevoked() &&
                    jwtService.isTokenValid(token, user)) {

                // Mark the user's account as verified
                user.setIsAccountVerified(Boolean.TRUE);
                userRepository.save(user);

                // Expire the token after successful verification
                tokenEntity.setExpired(Boolean.TRUE);
                tokenRepository.save(tokenEntity);

                return Boolean.TRUE;
            }
        }

        throw new RuntimeException("Invalid or expired token.");
    }


    @Transactional
    public Boolean confirmEmailRequest(HttpServletRequest httpServletRequest) throws MessagingException, IOException {
        User user = getUserByToken(httpServletRequest, jwtService, this.userRepository);

        // Generate a reset token
        String resetToken = jwtService.generateResetToken(user);
        String resetLink = httpServletRequest.getRequestURL().toString() + "/reset-password?token=" + resetToken;

        // Save the reset token in the database
        saveUserToken(user, resetToken, TokenType.RESET);

        // Determine the name to use in the email
        String name;
        if (user.getRole() == Role.COORDINATOR) {
            Coordinator coordinator = coordinatorRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Coordinator not found with email : " + user.getEmail()));
            name = coordinator.getFirstName() + " " + coordinator.getLastName();
        } else if (user.getRole() == Role.CUSTOMER) {
            Customer customer = customerRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with email: " + user.getEmail()));
            name = customer.getFirstName() + " " + customer.getLastName();
        } else {
            name = user.getEmail(); // Default to email if name is not available
        }

        // Send the password reset email
        emailService.sendPasswordResetEmail(name, user.getEmail(), resetLink);

        return Boolean.TRUE;
    }

}
