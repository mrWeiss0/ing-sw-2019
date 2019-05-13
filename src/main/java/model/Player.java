package model;

import connection.rmi.RemoteView;

public class Player {
    private String name;
    private RemoteView view;

    public Player(String n, RemoteView vv) {
        name = n;
        view = vv;
    }

    public String getName() {
        return name;
    }

    public RemoteView getView() {
        return view;
    }

    public void setView(RemoteView view) {
        this.view = view;
    }
}
