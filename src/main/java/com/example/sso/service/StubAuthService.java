package com.example.sso.service;

import com.example.sso.dto.UserDetailsDTO;
import org.springframework.stereotype.Service;

@Service
public class StubAuthService implements AuthService {

    @Override
    public AuthResult authenticate(String realm, String username, String password) {

        if ("locked".equalsIgnoreCase(username)) {
            return new AuthResult(false, "User locked", false, false, null);
        }

        if (!"pass".equals(password)) {
            return new AuthResult(false, "Invalid credentials (hint: password is 'pass')", false, false, null);
        }

        boolean mfa = "BC".equalsIgnoreCase(realm);
        boolean pwdChange = "PC".equalsIgnoreCase(realm);

        UserDetailsDTO u = new UserDetailsDTO();
        u.setUserName(username);
        u.setRealm(realm);

        return new AuthResult(true, "OK", mfa, pwdChange, u);
    }
}
