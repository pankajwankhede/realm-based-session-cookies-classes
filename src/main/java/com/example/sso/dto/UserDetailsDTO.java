package com.example.sso.dto;

import java.io.Serializable;

public class UserDetailsDTO implements Serializable {
    private String userName;
    private String realm;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }
}
