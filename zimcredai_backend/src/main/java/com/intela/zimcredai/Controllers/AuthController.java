package com.intela.zimcredai.Controllers;

import com.intela.zimcredai.Models.Coordinator;
import com.intela.zimcredai.Models.Customer;
import com.intela.zimcredai.RequestResponseModels.*;
import com.intela.zimcredai.Services.AuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /*
    *  Register and logins
    * */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ){
        AuthenticationResponse response = this.authService.register(request);
        return ResponseEntity.ok()
                .body(new ApiResponse<AuthenticationResponse>(null,"User registered successfully", response));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticateRequest request
    ){
        return ResponseEntity.accepted()
                .body(authService.authenticate(request));
    }

    @PostMapping("/refreshToken")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response

    ) throws IOException {
        authService.refreshToken(request, response);
    }


    /*
    *
    * Account details
    * HttpServletRequest will get the logged in person
    * So no one un authenticated can access these endpoints
    * */

    // Confirm email, confirm account
    @PostMapping("/confirmEmail/{token}")
    public ResponseEntity<?> confirmEmail(
            HttpServletRequest httpServletRequest,
            @PathVariable String token
    ){
        return ResponseEntity.ok()
                .body(this.authService.confirmEmail(httpServletRequest, token));
    }

    // Sent request to confirm email
    @PostMapping("/confirmEmailRequest")
    public ResponseEntity<?> confirmEmailRequest(HttpServletRequest httpServletRequest) throws MessagingException, IOException {
        return ResponseEntity.ok()
                .body(this.authService.confirmEmailRequest(httpServletRequest));
    }

    // Update credentials (email and/or password)
    @PostMapping("/updateCredentialsRequest")
    public ResponseEntity<?> updateCredentials(
            @RequestBody UpdateCredentials request,
            HttpServletRequest httpRequest
    ) throws MessagingException, IOException {
        return ResponseEntity.ok(authService.updateCredentials(request, httpRequest));
    }

    @GetMapping("/updateCredentials")
    public ResponseEntity<String> confirmCredentialsUpdate(@RequestParam String token, HttpServletRequest request) {
        try {
            boolean result = authService.confirmCredentialsUpdate(token);

            if (result) {
                return ResponseEntity.ok("Credentials update confirmed successfully.");
            } else {
                return ResponseEntity.status(400).body("Failed to confirm credentials update.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid or expired token.");
        }
    }


    // Delete account
    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount(HttpServletRequest request) {
        authService.deleteAccount(request);
        return ResponseEntity.ok("Account deleted successfully");
    }

    // Deactivate account
    @PatchMapping("/deactivateAccount")
    public ResponseEntity<String> deactivateAccount(HttpServletRequest request) {
        authService.deactivateAccount(request);
        return ResponseEntity.ok("Account deactivated successfully");
    }

    // Activate account
    @PatchMapping("/activateAccount")
    public ResponseEntity<String> activateAccount(HttpServletRequest request) {
        authService.activateAccount(request);
        return ResponseEntity.ok("Account activated successfully");
    }

    /*
    * Fetch Logged in user information
    * */

    // Get Logged in user credentials
    @GetMapping("/getLoggedUserCredentials")
    public ResponseEntity<LoggedUserResponse> getLoggedInUser(HttpServletRequest request){
        return ResponseEntity.ok(this.authService.fetchLoggedInUserByToken(request));
    }

    // Get Logged in Coordinator details (get by email)
    @GetMapping("/fetchLoggedInCoordinatorProfile/{email}")
    public ResponseEntity<Coordinator> fetchCoordinatorProfile(
            @PathVariable String email
    ){
        return ResponseEntity.ok()
                .body(this.authService.fetchCoordinatorDetails(email));
    }

    // Get Logged in Customer full details (get by email)
    @GetMapping("/fetchLoggedInCustomerProfile/{email}")
    public ResponseEntity<Customer> fetchCustomerProfile(
            @PathVariable String email
    ){
        return ResponseEntity.ok()
                .body(this.authService.fetchCustomerDetails(email));
    }

}

