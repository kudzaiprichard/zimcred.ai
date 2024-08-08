package com.intela.zimcredai.Controllers;

import com.intela.zimcredai.RequestResponseModels.*;
import com.intela.zimcredai.Services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping("/loggedUser")
    public ResponseEntity<LoggedUserResponse> getLoggedInUser(HttpServletRequest request){
        return ResponseEntity.ok(this.authService.fetchLoggedInUserByToken(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.created(URI.create("")).body(authService.register(request));
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

    // Todo: update credentials
    // Todo: rest password
    // Todo: delete account
    // Todo: deactivate account
    // Todo: activate account

    // Get Logged in Coordinator details (get by email)
    @GetMapping("/fetchLoggedInCoordinatorProfile/{email}")
    public ResponseEntity<CoordinatorProfileResponse> fetchCoordinatorProfile(
            @PathVariable String email
    ){
        return ResponseEntity.ok()
                .body(this.authService.fetchCoordinatorDetails(email));
    }

    // Get Logged in Customer full details (get by email)
    @GetMapping("/fetchLoggedInCustomerProfile/{email}")
    public ResponseEntity<CustomerProfileResponse> fetchCustomerProfile(
            @PathVariable String email
    ){
        return ResponseEntity.ok()
                .body(this.authService.fetchCustomerDetails(email));
    }

}

