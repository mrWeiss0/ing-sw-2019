package model;

import model.board.Figure;

public class Player {
    private Figure figure;

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public Figure getFigure() {
        return figure;
    }
}
