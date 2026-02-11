package com.example.sso.util;

public final class SessionKeys {
    private SessionKeys() {}

    public static final String HOME_DTO = "HOME_DTO";
    public static final String REALM = "REALM";

    // Multi-tab safe: state -> AuthzRequest
    public static final String AUTHZ_REQ_MAP = "AUTHZ_REQ_MAP";
    public static final String LAST_FLOW_KEY = "LAST_FLOW_KEY";
}
