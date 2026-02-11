package com.example.sso.dto;

import java.io.Serializable;
import java.time.Instant;

public class AuthzRequest implements Serializable {
    private String realm;          // realm for this flow
    private String clientId;       // partner client id
    private String responseType;
    private String redirectUrl;
    private String state;
    private String scope;

    private Instant createdAt = Instant.now();

    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getResponseType() { return responseType; }
    public void setResponseType(String responseType) { this.responseType = responseType; }

    public String getRedirectUrl() { return redirectUrl; }
    public void setRedirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
