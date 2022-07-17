package com.plugspace.common;

public interface AuthenticationListener {
    void onTokenReceived(String auth_token);
}
