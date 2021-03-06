package com.chatClientAndServer.server.authentication;

import com.chatClientAndServer.server.models.User;

import java.util.List;

public class BaseAuthenticationService implements AuthenticationService{

    private static final List<User> clients = List.of(
            new User("martin", "1111", "Martin_Cat"),
            new User("batman", "2222", "Брюс_Уэйн"),
            new User("gena", "3333", "Гендальф_Серый")
    );

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
        for (User client : clients) {
            if (client.getLogin().equals(login) && client.getPassword().equals(password)) {
                return client.getUsername();
            }
        }
        return null;
    }

    @Override
    public void createUser(String login, String password, String username) {

    }

    @Override
    public void updateUsername(String login, String newUsername) {

    }

    @Override
    public Boolean checkLoginByFree(String login) {
        return null;
    }

    @Override
    public void startAuthentication() {
        System.out.println("Старт аутентификации");

    }

    @Override
    public void endAuthentication() {
        System.out.println("Конец аутентификации");

    }
}
