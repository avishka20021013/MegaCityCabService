package com.example.megaCabService.components;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.example.megaCabService.models.ClientModel;

@Component
@SessionScope
public class SessionData {
    private ClientModel loggedClient;

    public ClientModel getLoggedClient() {
        return loggedClient;
    }

    public void setLoggedClient(ClientModel loggedClient) {
        this.loggedClient = loggedClient;
    }
}
