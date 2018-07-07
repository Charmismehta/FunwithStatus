package com.epsilon.FunwithStatus.jsonpojo.registration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationDatum {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user")
    @Expose
    private RegistrationUser user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RegistrationUser getUser() {
        return user;
    }

    public void setUser(RegistrationUser user) {
        this.user = user;
    }

}
