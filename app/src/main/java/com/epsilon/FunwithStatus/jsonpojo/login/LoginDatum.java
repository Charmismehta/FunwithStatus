package com.epsilon.FunwithStatus.jsonpojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDatum {

    @SerializedName("User")
    @Expose
    private LoginUser user;

    public LoginUser getUser() {
        return user;
    }

    public void setUser(LoginUser user) {
        this.user = user;
    }
}
