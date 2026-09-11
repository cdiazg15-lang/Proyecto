package com.mycompany.totito;
import java.util.Scanner;

public class Juego {

    private Tablero tablero;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador jugadorActual;
    
    // Atributo para guardar la Inteligencia Artificial
    private IA_Minimax ia; 

    // Constructor modificado para recibir la IA
    public Juego(Jugador jugador1, Jugador jugador2, IA_Minimax ia) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.ia = ia; // Guardamos la instancia de la IA
        this.tablero = new Tablero();
        this.jugadorActual = jugador1;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public boolean realizarJugada(int fila, int columna) {
        if (fila < 0 || fila > 2) return false;
        if (columna < 0 || columna > 2) return false;
        
        if (tablero.casillaDisponible(fila, columna)) {
            tablero.colocarFicha(fila, columna, jugadorActual.getFicha());
            return true;
        }
        return false;
    }

    public void cambiarTurno() {
        if (jugadorActual == jugador1) {
            jugadorActual = jugador2;
        } else {
            jugadorActual = jugador1;
        }
    }

    public boolean jugadorGano() {
        return tablero.hayGanador(jugadorActual.getFicha());
    }

    public boolean hayEmpate() {
        return tablero.tableroLleno() && !jugadorGano();
    }

    public void mostrarTablero() {
        tablero.mostrarTablero();
    }

    public void reiniciar() {
        tablero.reiniciarTablero();
        jugadorActual = jugador1;
    }

    public void jugar() {
        Scanner scanner = new Scanner(System.in);
        boolean partidaTerminada = false;
        
        System.out.println("\n¡Comienza el juego! Las 'X' tienen el primer turno.");

        while (!partidaTerminada) {
            mostrarTablero();
            System.out.println("\nTurno de: " + jugadorActual.getNombre() + " (" + jugadorActual.getFicha() + ")");

            int fila, columna;

            // Verificamos si el jugador del turno actual es la máquina
            if (jugadorActual.getNombre().equals("IA Minimax")) {
                System.out.println("La IA esta analizando sus posibilidades...");
                
                // La máquina elige su jugada
                int[] movimiento = ia.obtenerMejorMovimiento(tablero);
                fila = movimiento[0];
                columna = movimiento[1];
            } else {
                // Si no es la máquina, es el humano. Pedimos datos por teclado.
                System.out.print("Ingrese la fila (1-3): ");
                fila = scanner.nextInt() - 1;

                System.out.print("Ingrese la columna (1-3): ");
                columna = scanner.nextInt() - 1;
            }

            if (realizarJugada(fila, columna)) {
                if (jugadorGano()) {
                    mostrarTablero();
                    System.out.println("\n¡" + jugadorActual.getNombre() + " ha ganado!");
                    partidaTerminada = true;
                } else if (hayEmpate()) {
                    mostrarTablero();
                    System.out.println("\nLa partida termino en empate!");
                    partidaTerminada = true;
                } else {
                    cambiarTurno();
                }
            } else {
                if (fila < 0 || fila > 2 || columna < 0 || columna > 2) {
                    System.out.println("\nLa posicion ingresada no es valida.");
                } else {
                    System.out.println("\nEsa casilla ya esta ocupada.");
                }
            }
        }
    }
}