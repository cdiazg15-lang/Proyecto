package com.mycompany.totito;

public class Jugador {

    // Nombre del jugador.
    private String nombre;

    // Ficha que utilizará el jugador: 'X' o 'O'.
    private char ficha;

    // Constructor de la clase Jugador.
    public Jugador(String nombre, char ficha) {

        // Guardamos el nombre recibido.
        this.nombre = nombre;

        // Guardamos la ficha recibida.
        this.ficha = ficha;
    }

    // Retorna el nombre del jugador.
    public String getNombre() {
        return nombre;
    }

    // Retorna la ficha del jugador.
    public char getFicha() {
        return ficha;
    }
}