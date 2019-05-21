package model;

import connection.rmi.RemoteView;
import connection.server.VirtualView;
import model.board.Figure;


public class Player {
    private String name;
    private VirtualView view;
    private Figure figure;
    private String id;

    public Player(String n, String id, VirtualView vv) {
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

    public VirtualView getView() {
        return view;
    }

    public void setView(VirtualView view) {
        this.view = view;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }
}

