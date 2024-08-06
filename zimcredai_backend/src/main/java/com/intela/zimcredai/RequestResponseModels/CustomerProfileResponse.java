package com.intela.zimcredai.RequestResponseModels;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProfileResponse {
    public String firstName;
    public String lastName;
    // Todo: Add more attributes
}
