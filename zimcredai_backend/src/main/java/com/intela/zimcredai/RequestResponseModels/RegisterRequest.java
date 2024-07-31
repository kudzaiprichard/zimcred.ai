package com.intela.zimcredai.RequestResponseModels;

import com.intela.zimcredai.Models.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;
}
