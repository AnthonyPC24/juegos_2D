package Juego;

import java.awt.*;

public class Plataforma {
    public int posPlatfornX, posPlatformY, width, height;

    public Plataforma(int posPlatfornX, int posPlatformY, int width, int height) {
        this.posPlatfornX = posPlatfornX;
        this.posPlatformY = posPlatformY;
        this.width = width;
        this.height = height;
    }

    // Uso esta clase para representar las areas de colicion de los objetos del juego
    // me ayuda a detectar si dos objetos se estan tocando
    public Rectangle getBounds() {
        return new Rectangle(posPlatfornX, posPlatformY, width, height);
    }
}
