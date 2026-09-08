package com.mycompany.totito;

public class Tablero {

    // Matriz que representa las 9 casillas del tablero.
    // Cada posición puede contener 'X', 'O' o estar vacía.
    private char[][] tablero;

    // Constructor de la clase Tablero.
    public Tablero() {

        // Creamos una matriz de 3 filas y 3 columnas.
        tablero = new char[3][3];

        // Recorremos todas las posiciones del tablero.
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {

                // Inicialmente todas las casillas estarán vacías.
                tablero[fila][columna] = ' ';
            }
        }
    }

    // Coloca una ficha ('X' o 'O') en una posición determinada del tablero.
    public void colocarFicha(int fila, int columna, char ficha) {

        // Guardamos la ficha en la posición indicada.
        tablero[fila][columna] = ficha;
    }
    
        // Verifica si una casilla del tablero está vacía.
    public boolean casillaDisponible(int fila, int columna) {

        // Retorna verdadero si la casilla está vacía.
        return tablero[fila][columna] == ' ';
    }
    
    // Retorna la ficha que se encuentra en una posición del tablero.
public char obtenerFicha(int fila, int columna) {

    // Devolvemos el contenido de la casilla indicada.
    return tablero[fila][columna];
}

// Crea y retorna una copia independiente del tablero actual.
public Tablero copiar() {

    // Creamos un nuevo tablero vacío.
    Tablero copia = new Tablero();

    // Recorremos todas las filas.
    for (int fila = 0; fila < 3; fila++) {

        // Recorremos todas las columnas.
        for (int columna = 0; columna < 3; columna++) {

            // Obtenemos la ficha de la casilla actual.
            char ficha = this.tablero[fila][columna];

            // Si la casilla no está vacía, copiamos la ficha.
            if (ficha != ' ') {
                copia.colocarFicha(fila, columna, ficha);
            }
        }
    }

    // Retornamos el nuevo tablero independiente.
    return copia;
}
    
        // Verifica si la ficha indicada ha conseguido tres posiciones consecutivas.
    public boolean hayGanador(char ficha) {

        // Verificamos las tres filas.
        for (int fila = 0; fila < 3; fila++) {

            if (tablero[fila][0] == ficha &&
                tablero[fila][1] == ficha &&
                tablero[fila][2] == ficha) {

                return true;
            }
        }

        // Verificamos las tres columnas.
        for (int columna = 0; columna < 3; columna++) {

            if (tablero[0][columna] == ficha &&
                tablero[1][columna] == ficha &&
                tablero[2][columna] == ficha) {

                return true;
            }
        }

        // Verificamos la diagonal principal.
        if (tablero[0][0] == ficha &&
            tablero[1][1] == ficha &&
            tablero[2][2] == ficha) {

            return true;
        }

        // Verificamos la diagonal secundaria.
        if (tablero[0][2] == ficha &&
            tablero[1][1] == ficha &&
            tablero[2][0] == ficha) {

            return true;
        }

        // Si no se encontró ninguna combinación ganadora.
        return false;
    }
    
        // Verifica si todas las casillas del tablero están ocupadas.
    public boolean tableroLleno() {

        // Recorremos todas las filas.
        for (int fila = 0; fila < 3; fila++) {

            // Recorremos todas las columnas.
            for (int columna = 0; columna < 3; columna++) {

                // Si encontramos una casilla vacía, el tablero no está lleno.
                if (tablero[fila][columna] == ' ') {
                    return false;
                }
            }
        }

        // Si no encontramos ninguna casilla vacía, el tablero está lleno.
        return true;
    }

    // Reinicia todas las casillas del tablero.
public void reiniciarTablero() {

    // Recorremos todas las filas.
    for (int fila = 0; fila < 3; fila++) {

        // Recorremos todas las columnas.
        for (int columna = 0; columna < 3; columna++) {

            // Dejamos nuevamente la casilla vacía.
            tablero[fila][columna] = ' ';
        }
    }
}
    // Muestra el tablero en la consola.
    public void mostrarTablero() {

        // Recorremos las filas del tablero.
        for (int fila = 0; fila < 3; fila++) {

            // Mostramos las casillas de cada fila.
            System.out.println(
                    " " + tablero[fila][0] +
                    " | " + tablero[fila][1] +
                    " | " + tablero[fila][2]
            );

            // Mostramos las líneas que separan las filas.
            if (fila < 2) {
                System.out.println("---+---+---");
            }
        }
    }
}