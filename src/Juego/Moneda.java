package Juego;

import java.awt.*;

public class Moneda {
    private int x, y;

    public Moneda(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 20, 20);
    }
}
