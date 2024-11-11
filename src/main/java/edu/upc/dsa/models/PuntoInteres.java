package edu.upc.dsa.models;

public class PuntoInteres {
    private int coordenadaX;
    private int coordenadaY;
    private ElementType tipo;

    // Constructor
    public PuntoInteres(int coordenadaX, int coordenadaY, ElementType tipo) {
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.tipo = tipo;
    }

    // Getters y Setters
    public int getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(int coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public int getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(int coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public ElementType getTipo() {
        return tipo;
    }

    public void setTipo(ElementType tipo) {
        this.tipo = tipo;
    }
}
