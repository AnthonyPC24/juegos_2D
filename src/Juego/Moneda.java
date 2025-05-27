package Juego;

import java.awt.*;

public class Moneda {
    public static final int ANCHO = 40;
    public static final int ALTO = 60;

    private final int posMonedaX;
    private final int posMonedaY;

    public Moneda(int posMonedaX, int posMonedaY) {
        this.posMonedaX = posMonedaX;
        this.posMonedaY = posMonedaY;
    }

    public int getPosMonedaX() { return posMonedaX; }
    public int getPosMonedaY() { return posMonedaY; }

    public Rectangle getBounds() {
        return new Rectangle(posMonedaX, posMonedaY, ANCHO, ALTO);
    }
}
