package Juego;

public class Principiante extends Personaje{

    public Principiante(String nombre) {
        super(nombre);
        this.tipo = "Principiante";
        this.bonusPuntos = 10;
        this.salto = 15;
    }
}
