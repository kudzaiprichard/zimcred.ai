package com.intela.zimcredai.RequestResponseModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCredentials {
    private String email;
    private String password;
    private String confirmPassword;
}
