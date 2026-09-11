package com.mycompany.totito;

public class IA_Minimax {

    // Encapsulamiento de los atributos necesarios para la toma de decisiones
    private char fichaIA;
    private char fichaOponente;

    public IA_Minimax(char fichaIA, char fichaOponente) {
        this.fichaIA = fichaIA;
        this.fichaOponente = fichaOponente;
    }

    // Método principal que evaluará el tablero y devolverá la {fila, columna} ideal
    public int[] obtenerMejorMovimiento(Tablero tablero) {
        int mejorPuntaje = Integer.MIN_VALUE;
        int[] mejorMovimiento = new int[]{-1, -1};

        // Recorremos la matriz 3x3
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                
                if (tablero.casillaDisponible(fila, columna)) {
                    // Creamos un universo paralelo (copia) para probar este movimiento
                    Tablero copia = tablero.copiar();
                    copia.colocarFicha(fila, columna, fichaIA);
                    
                    // Calculamos el puntaje de esta jugada usando recursividad
                    int puntaje = minimax(copia, 0, false);
                    
                    // Si el puntaje es mejor que el anterior, actualizamos la jugada
                    if (puntaje > mejorPuntaje) {
                        mejorPuntaje = puntaje;
                        mejorMovimiento[0] = fila;
                        mejorMovimiento[1] = columna;
                    }
                }
            }
        }
        return mejorMovimiento;
    }

    // Algoritmo Minimax recursivo
    private int minimax(Tablero tablero, int profundidad, boolean esMaximizador) {
        // 1. Casos base: Evaluación de estados finales del tablero
        if (tablero.hayGanador(fichaIA)) {
            return 10 - profundidad; // Restamos profundidad para preferir victorias rápidas
        }
        if (tablero.hayGanador(fichaOponente)) {
            return -10 + profundidad; // Sumamos profundidad para alargar la derrota lo más posible
        }
        if (tablero.tableroLleno()) {
            return 0; // Empate
        }

        // 2. Turno de la IA (Maximizador: busca el puntaje más alto)
        if (esMaximizador) {
            int mejorPuntaje = Integer.MIN_VALUE;
            for (int fila = 0; fila < 3; fila++) {
                for (int columna = 0; columna < 3; columna++) {
                    if (tablero.casillaDisponible(fila, columna)) {
                        Tablero copia = tablero.copiar();
                        copia.colocarFicha(fila, columna, fichaIA);
                        mejorPuntaje = Math.max(mejorPuntaje, minimax(copia, profundidad + 1, false));
                    }
                }
            }
            return mejorPuntaje;
            
        // 3. Turno del Oponente (Minimizador: busca el puntaje más bajo)
        } else {
            int mejorPuntaje = Integer.MAX_VALUE;
            for (int fila = 0; fila < 3; fila++) {
                for (int columna = 0; columna < 3; columna++) {
                    if (tablero.casillaDisponible(fila, columna)) {
                        Tablero copia = tablero.copiar();
                        copia.colocarFicha(fila, columna, fichaOponente);
                        mejorPuntaje = Math.min(mejorPuntaje, minimax(copia, profundidad + 1, true));
                    }
                }
            }
            return mejorPuntaje;
        }
    }
}