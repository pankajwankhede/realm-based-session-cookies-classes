package com.example.sso.security;

import jakarta.servlet.http.HttpServletRequest;

public interface RealmResolver {
    String resolveRealm(HttpServletRequest request);
}
