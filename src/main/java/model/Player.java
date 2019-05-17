package model;

import connection.rmi.RemoteView;
import model.board.Figure;


public class Player {
    private String name;
    private RemoteView view;
    private Figure figure;
    private String id;

    public Player(String n, String id,RemoteView vv) {
        name = n;
        this.id= id;
        view = vv;
    }
    public String getId(){
        return id;
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

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }
}

