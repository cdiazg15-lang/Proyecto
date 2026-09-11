package com.mycompany.totito;
import java.util.Scanner;

public class Totito {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=========================================");
        System.out.println("       JUGADOR VS INTELIGENCIA ARTIFICIAL ");
        System.out.println("=========================================");
        System.out.print("Que ficha deseas utilizar? ('X' o 'O'): ");
        
        // Leemos la ficha y validamos que el usuario ingrese X o O
        char fichaHumano = scanner.next().toUpperCase().charAt(0);
        while (fichaHumano != 'X' && fichaHumano != 'O') {
            System.out.print("Ficha no valida. Ingresa 'X' o 'O': ");
            fichaHumano = scanner.next().toUpperCase().charAt(0);
        }
        
        char fichaIA = (fichaHumano == 'X') ? 'O' : 'X';
        
        Jugador jugador1;
        Jugador jugador2;
        
        // En el Totito, las 'X' siempre inician. 
        // Asignamos el Jugador 1 al que tenga la ficha 'X'
        if (fichaHumano == 'X') {
            jugador1 = new Jugador("Humano", 'X');
            jugador2 = new Jugador("IA Minimax", 'O');
        } else {
            jugador1 = new Jugador("IA Minimax", 'X');
            jugador2 = new Jugador("Humano", 'O');
        }

        // Instanciamos tu Inteligencia Artificial
        IA_Minimax ia = new IA_Minimax(fichaIA, fichaHumano);

        // Creamos la partida inyectando los jugadores y la IA
        Juego juego = new Juego(jugador1, jugador2, ia);

        // Iniciamos el juego
        juego.jugar();
    }
}