package br.com.progiv.chat;

public class User {

    private int userId;
    private String username;


    public User(int usersId, String name) {
        this.userId = usersId;
        this.username = name;

    }

    public int getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }



}
