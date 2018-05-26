package com.epsilon.FunwithStatus.jsonpojo.registration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationDatum {
    @SerializedName("User")
    @Expose
    private RegistrationUser user;

    public RegistrationUser getUser() {
        return user;
    }

    public void setUser(RegistrationUser user) {
        this.user = user;
    }
}
