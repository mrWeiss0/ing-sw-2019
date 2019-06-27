package client.model;

public class PowerUp {
    private int color;
    private int type;

    public PowerUp(int type, int color){
        this.color=color;
        this.type=type;
    }

    public int getColor() {
        return color;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return type+" "+color;
    }
}
