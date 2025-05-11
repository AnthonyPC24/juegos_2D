package Juego;

public abstract class Personaje {
    private int id;
    protected String nombre;
    protected String tipo;
    protected int bonusPuntos;
    protected int salto;

    // Constructor mejorado
    public Personaje(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public int getSalto() {
        return salto;
    }

    public void setSalto(int salto) {
        this.salto = salto;
    }

}

