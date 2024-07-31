package com.intela.zimcredai.RequestResponseModels;

import com.intela.zimcredai.Models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoggedUserResponse {
    private Boolean isAuthenticated;
    private String email;
    private Role role;
}
