package com.mycompany.totito;
import java.util.Scanner;

public class Juego {

    // Tablero donde se desarrollará la partida.
    private Tablero tablero;

    // Primer jugador.
    private Jugador jugador1;

    // Segundo jugador.
    private Jugador jugador2;

    // Jugador que tiene el turno actualmente.
    private Jugador jugadorActual;

    // Constructor de la clase Juego.
    public Juego(Jugador jugador1, Jugador jugador2) {

        // Guardamos el primer jugador.
        this.jugador1 = jugador1;

        // Guardamos el segundo jugador.
        this.jugador2 = jugador2;

        // Creamos un tablero nuevo para la partida.
        this.tablero = new Tablero();

        // El primer jugador comienza la partida.
        this.jugadorActual = jugador1;
    }
    
        // Retorna el jugador que tiene el turno actualmente.
    public Jugador getJugadorActual() {

        // Devolvemos el jugador actual.
        return jugadorActual;
    }
    
    // Retorna el tablero actual de la partida.
public Tablero getTablero() {

    // Devolvemos el tablero actual.
    return tablero;
}
    
     public boolean realizarJugada(int fila, int columna) {

    // Verificamos que la fila esté dentro de los límites del tablero.
    if (fila < 0 || fila > 2) {
        return false;
    }

    // Verificamos que la columna esté dentro de los límites del tablero.
    if (columna < 0 || columna > 2) {
        return false;
    }

    // Verificamos si la casilla está disponible.
    if (tablero.casillaDisponible(fila, columna)) {

        // Colocamos la ficha del jugador actual.
        tablero.colocarFicha(fila, columna, jugadorActual.getFicha());

        // La jugada se realizó correctamente.
        return true;
    }

    // La jugada no se pudo realizar.
    return false;
}
    
        // Cambia el turno al otro jugador.
    public void cambiarTurno() {

        // Si el jugador actual es el jugador 1,
        // el turno pasa al jugador 2.
        if (jugadorActual == jugador1) {
            jugadorActual = jugador2;
        } else {

            // Si no, el turno vuelve al jugador 1.
            jugadorActual = jugador1;
        }
        
    }
    
        // Verifica si el jugador actual ha ganado la partida.
    public boolean jugadorGano() {

        // Consultamos al tablero si la ficha del jugador actual
        // consiguió una combinación ganadora.
        return tablero.hayGanador(jugadorActual.getFicha());
    }
    
        // Verifica si el tablero está lleno y no hay un ganador.
    public boolean hayEmpate() {

        // Si todas las casillas están ocupadas y no hay ganador,
        // significa que la partida terminó en empate.
        return tablero.tableroLleno() && !jugadorGano();
    }
    
        // Muestra el tablero actual de la partida.
    public void mostrarTablero() {

        // Le pedimos al tablero que muestre sus casillas.
        tablero.mostrarTablero();
    }
    
    // Reinicia el tablero y establece nuevamente el turno del primer jugador.
public void reiniciar() {

    // Limpiamos todas las casillas del tablero.
    tablero.reiniciarTablero();

    // El primer jugador vuelve a comenzar la partida.
    jugadorActual = jugador1;
}
    
        // Inicia y controla la partida completa.
    public void jugar() {

        // Creamos un Scanner para recibir datos del teclado.
        Scanner scanner = new Scanner(System.in);

        // Variable que indica si la partida continúa.
        boolean partidaTerminada = false;

        // Repetimos los turnos mientras la partida no termine.
        while (!partidaTerminada) {

            // Mostramos el tablero antes de cada jugada.
            mostrarTablero();

            // Mostramos el jugador que tiene el turno.
            System.out.println("\nTurno de: "
                    + jugadorActual.getNombre()
                    + " (" + jugadorActual.getFicha() + ")");

            // Pedimos la fila al jugador.
            System.out.print("Ingrese la fila (1-3): ");
            int fila = scanner.nextInt();

            // Pedimos la columna al jugador.
            System.out.print("Ingrese la columna (1-3): ");
            int columna = scanner.nextInt();

            // Convertimos las posiciones de 1-3 a posiciones de 0-2.
            fila--;
            columna--;

            // Intentamos realizar la jugada.
if (realizarJugada(fila, columna)) {

    // Verificamos si el jugador consiguió tres fichas.
    if (jugadorGano()) {

        // Mostramos el tablero final.
        mostrarTablero();

        // Informamos quién ganó.
        System.out.println("\n¡"
                + jugadorActual.getNombre()
                + " ha ganado!");

        // Terminamos la partida.
        partidaTerminada = true;

    // Si no ganó, verificamos si hubo empate.
    } else if (hayEmpate()) {

        // Mostramos el tablero final.
        mostrarTablero();

        // Informamos que la partida terminó en empate.
        System.out.println("\n¡La partida terminó en empate!");

        // Terminamos la partida.
        partidaTerminada = true;

    } else {

        // Si nadie ganó y no hay empate,
        // cambiamos el turno.
        cambiarTurno();
    }

} else {

    // Informamos que la posición ingresada no es válida.
    if (fila < 0 || fila > 2 || columna < 0 || columna > 2) {

        System.out.println("\nLa posición ingresada no es válida.");

    } else {

        // Informamos que la casilla ya está ocupada.
        System.out.println("\nEsa casilla ya está ocupada.");
    }
}
        }
    }
}
