package com.example.sso.service;

import com.example.sso.dto.UserDetailsDTO;

public interface AuthService {
    AuthResult authenticate(String realm, String username, String password);

    record AuthResult(
            boolean success,
            String message,
            boolean mfaRequired,
            boolean passwordChangeRequired,
            UserDetailsDTO userDetails
    ) {}
}
