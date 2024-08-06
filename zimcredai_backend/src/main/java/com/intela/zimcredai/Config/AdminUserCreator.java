package com.intela.zimcredai.Config;

import com.intela.zimcredai.Controllers.AuthController;
import com.intela.zimcredai.Models.Role;
import com.intela.zimcredai.RequestResponseModels.AuthenticationResponse;
import com.intela.zimcredai.RequestResponseModels.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserCreator implements CommandLineRunner {
    private final AuthController authController;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String email = "admin@gmail.com";
        String password = "admin";

        // create default system admin user
        RegisterRequest request = RegisterRequest.builder()
                .role(Role.ADMIN)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        AuthenticationResponse res = authController.register(request).getBody();

        System.out.println("-----------------------------------------------------");
        System.out.println("Default system admin user created successfully....");
        System.out.println("Use below credentials to login");
        System.out.println("Email / Username: " + email);
        System.out.println("Password: " + password );
        System.out.println("Created At: " + res.getCreatedAt() );

        assert res != null;
        System.out.println("-----------------------------------------------------");
        System.out.println("Or use AccessToken: " + res.getAccessToken());
        System.out.println("-----------------------------------------------------");
    }
}
