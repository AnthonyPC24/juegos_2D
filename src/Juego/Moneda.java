package Juego;

import java.awt.*;

public class Moneda {
    public static final int ANCHO = 40;
    public static final int ALTO = 60;

    private int x, y;

    public Moneda(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, ANCHO, ALTO);
    }
}
