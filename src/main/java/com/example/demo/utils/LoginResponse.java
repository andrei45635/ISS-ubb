package com.example.demo.utils;

import com.example.demo.model.Programmer;
import com.example.demo.model.QA;

public class LoginResponse {
    private LoginType loginType;
    private Programmer prog;
    private QA qa;

    public LoginResponse(LoginType loginType) {
        this.loginType = loginType;
    }

    public LoginResponse(LoginType loginType, QA qa) {
        this.loginType = loginType;
        this.qa = qa;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public Programmer getProg() {
        return prog;
    }

    public void setProg(Programmer prog) {
        this.prog = prog;
    }

    public QA getQa() {
        return qa;
    }

    public void setQa(QA qa) {
        this.qa = qa;
    }
}
