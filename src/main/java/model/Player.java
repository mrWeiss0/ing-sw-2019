package model;

import connection.rmi.RemoteView;
import model.board.Figure;


public class Player {
    private String name;
    private RemoteView view;
    private Figure figure;


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

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }
}

