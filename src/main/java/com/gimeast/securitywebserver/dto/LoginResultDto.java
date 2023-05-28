package com.gimeast.securitywebserver.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginResultDto extends SignUpResultDto{
    private String token;

    @Builder
    public LoginResultDto(boolean success, int code, String msg, String token) {
        super(success, code, msg);
        this.token = token;
    }
}
