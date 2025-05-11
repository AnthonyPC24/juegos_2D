package Juego;

public class Escalador extends Personaje{

    public Escalador(String nombre) {
        super(nombre);
        this.tipo = "Escalador";
        this.bonusPuntos = 5;
        this.salto = 20;
    }
}
