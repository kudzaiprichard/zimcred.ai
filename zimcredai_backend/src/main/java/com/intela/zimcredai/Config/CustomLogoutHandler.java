package com.intela.zimcredai.Config;

import com.intela.zimcredai.Models.User;
import com.intela.zimcredai.Repositories.UserRepository;
import com.intela.zimcredai.Services.AuthService;
import com.intela.zimcredai.Services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.intela.zimcredai.Util.Util.getUserByToken;


@RequiredArgsConstructor
@Configuration
public class CustomLogoutHandler implements LogoutHandler {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
       User user = getUserByToken(request, jwtService, userRepository);
        authService.revokeAllUserTokens(user);
    }
}
