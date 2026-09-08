package com.mycompany.totito;

public class Totito {

    public static void main(String[] args) {
        // Creamos el primer jugador.
        Jugador jugador1 = new Jugador("Jugador 1", 'X');

        // Creamos el segundo jugador.
        Jugador jugador2 = new Jugador("Jugador 2", 'O');

        // Creamos una nueva partida.
        Juego juego = new Juego(jugador1, jugador2);

        // Iniciamos el juego.
        juego.jugar();
    }
}


    

