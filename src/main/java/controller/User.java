package controller;

import connection.rmi.RemoteView;


public class User {
    private String name;
    private RemoteView view;

    public User(String n, RemoteView vv) {
        name = n;
        view = vv;
    }

    public String getName() {
        return name;
    }

    public RemoteView getView() {
        return view;
    }
}
