package com.mycompany.totito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class InterfazGrafica extends JFrame {

    private JButton[][] botonesCasillas = new JButton[3][3];
    private JLabel etiquetaTurno;
    private JLabel etiquetaEstado;
    private JButton botonNuevaPartida;
    private JButton botonReiniciar;
    private JButton botonModoNoche;

    private JPanel panelSuperior;
    private JPanel panelTablero;
    private JPanel panelInferior;

    private Juego juego;
    private char fichaHumano;
    private char fichaIA;
    private boolean juegoTerminado = false;
    private boolean modoOscuro = false;

    // Paleta de colores
    private static final Color FONDO_CLARO = new Color(245, 245, 245);
    private static final Color PANEL_CLARO = new Color(230, 230, 230);
    private static final Color BOTON_CLARO = Color.WHITE;
    private static final Color TEXTO_CLARO = Color.BLACK;

    private static final Color FONDO_OSCURO = new Color(30, 30, 30);
    private static final Color PANEL_OSCURO = new Color(45, 45, 45);
    private static final Color BOTON_OSCURO = new Color(60, 60, 60);
    private static final Color TEXTO_OSCURO = new Color(230, 230, 230);

    public InterfazGrafica() {
        super("Totito - IA Minimax Paulo");
        construirInterfaz();
        aplicarTema();
        iniciarNuevaPartida();
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // ---------- Panel superior: turno, estado y boton modo noche ----------
        panelSuperior = new JPanel(new BorderLayout());

        JPanel panelTextos = new JPanel(new GridLayout(2, 1));
        etiquetaTurno = new JLabel("Turno: -", SwingConstants.CENTER);
        etiquetaTurno.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaEstado = new JLabel(" ", SwingConstants.CENTER);
        etiquetaEstado.setFont(new Font("Arial", Font.BOLD, 16));
        panelTextos.add(etiquetaTurno);
        panelTextos.add(etiquetaEstado);
        panelTextos.setOpaque(false);

        botonModoNoche = new JButton(new LunaIcon(18));
        botonModoNoche.setFocusPainted(false);
        botonModoNoche.setPreferredSize(new Dimension(46, 40));
        botonModoNoche.setToolTipText("Cambiar modo noche/dia");
        botonModoNoche.addActionListener(e -> alternarModoNoche());

        JPanel panelBotonNoche = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonNoche.setOpaque(false);
        panelBotonNoche.add(botonModoNoche);

        panelSuperior.add(panelTextos, BorderLayout.CENTER);
        panelSuperior.add(panelBotonNoche, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // ---------- Panel central: tablero 3x3 ----------
        panelTablero = new JPanel(new GridLayout(3, 3, 5, 5));
        panelTablero.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Font fuenteBoton = new Font("Arial", Font.BOLD, 40);

        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                JButton boton = new JButton("");
                boton.setFont(fuenteBoton);
                boton.setFocusPainted(false);
                boton.setPreferredSize(new Dimension(100, 100));
                final int f = fila, c = columna;
                boton.addActionListener((ActionEvent e) -> manejarClicCasilla(f, c));
                botonesCasillas[fila][columna] = boton;
                panelTablero.add(boton);
            }
        }
        add(panelTablero, BorderLayout.CENTER);

        // ---------- Panel inferior: controles ----------
        panelInferior = new JPanel(new FlowLayout());
        botonNuevaPartida = new JButton("Nueva partida");
        botonReiniciar = new JButton("Reiniciar");

        botonNuevaPartida.addActionListener(e -> iniciarNuevaPartida());
        botonReiniciar.addActionListener(e -> reiniciarPartida());

        panelInferior.add(botonNuevaPartida);
        panelInferior.add(botonReiniciar);
        add(panelInferior, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    // ==================== ICONOS DIBUJADOS: SOL Y LUNA ====================

    /** Icono de sol: circulo relleno con rayos alrededor. */
    private static class SolIcon implements Icon {
        private final int tam;

        SolIcon(int tam) {
            this.tam = tam;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 170, 0));

            int cx = x + tam;
            int cy = y + tam;
            int radioCentro = (int) (tam * 0.55);

            // Rayos
            g2.setStroke(new BasicStroke(2.2f));
            int radioRayoInterno = radioCentro + 3;
            int radioRayoExterno = tam;
            for (int i = 0; i < 8; i++) {
                double angulo = Math.toRadians(i * 45);
                int x1 = cx + (int) (radioRayoInterno * Math.cos(angulo));
                int y1 = cy + (int) (radioRayoInterno * Math.sin(angulo));
                int x2 = cx + (int) (radioRayoExterno * Math.cos(angulo));
                int y2 = cy + (int) (radioRayoExterno * Math.sin(angulo));
                g2.drawLine(x1, y1, x2, y2);
            }

            // Circulo central
            g2.fillOval(cx - radioCentro, cy - radioCentro, radioCentro * 2, radioCentro * 2);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return tam * 2;
        }

        @Override
        public int getIconHeight() {
            return tam * 2;
        }
    }

    /** Icono de luna: forma de luna creciente hecha restando dos circulos. */
    private static class LunaIcon implements Icon {
        private final int tam;

        LunaIcon(int tam) {
            this.tam = tam;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int diametro = tam * 2;
            Ellipse2D circuloCompleto = new Ellipse2D.Double(x, y, diametro, diametro);
            Ellipse2D circuloRecorte = new Ellipse2D.Double(x + tam * 0.65, y - tam * 0.15, diametro, diametro);

            Area luna = new Area(circuloCompleto);
            luna.subtract(new Area(circuloRecorte));

            g2.setColor(new Color(230, 220, 130));
            g2.fill(luna);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return tam * 2;
        }

        @Override
        public int getIconHeight() {
            return tam * 2;
        }
    }

    // ==================== MODO NOCHE / DIA ====================

    private void alternarModoNoche() {
        modoOscuro = !modoOscuro;
        aplicarTema();
    }

    private void aplicarTema() {
        Color fondo = modoOscuro ? FONDO_OSCURO : FONDO_CLARO;
        Color panel = modoOscuro ? PANEL_OSCURO : PANEL_CLARO;
        Color botonFondo = modoOscuro ? BOTON_OSCURO : BOTON_CLARO;
        Color texto = modoOscuro ? TEXTO_OSCURO : TEXTO_CLARO;

        getContentPane().setBackground(fondo);
        panelSuperior.setBackground(panel);
        panelTablero.setBackground(fondo);
        panelInferior.setBackground(panel);

        etiquetaTurno.setForeground(texto);
        if (!juegoTerminado) {
            etiquetaEstado.setForeground(texto);
        }

        // Si esta en modo oscuro, mostramos el icono de sol (para volver al dia);
        // si esta en modo claro, mostramos el icono de luna (para pasar a la noche).
        botonModoNoche.setIcon(modoOscuro ? new SolIcon(18) : new LunaIcon(18));
        botonModoNoche.setBackground(botonFondo);

        botonNuevaPartida.setBackground(botonFondo);
        botonNuevaPartida.setForeground(texto);
        botonReiniciar.setBackground(botonFondo);
        botonReiniciar.setForeground(texto);

        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                JButton boton = botonesCasillas[fila][columna];
                boton.setBackground(botonFondo);
                if (boton.getText().isEmpty()) {
                    boton.setForeground(texto);
                }
            }
        }

        repaint();
    }

    // ==================== PARTIDA ====================

    /** Pregunta con que ficha quiere jugar el humano y arma la partida desde cero. */
    private void iniciarNuevaPartida() {
        String[] opciones = {"X", "O"};
        int seleccion = JOptionPane.showOptionDialog(
                this,
                "¿Con qué ficha quieres jugar?",
                "Nueva partida",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        fichaHumano = (seleccion == 1) ? 'O' : 'X';
        fichaIA = (fichaHumano == 'X') ? 'O' : 'X';

        Jugador jugador1;
        Jugador jugador2;

        // Las 'X' siempre inician, igual que en Totito.java
        if (fichaHumano == 'X') {
            jugador1 = new Jugador("Humano", 'X');
            jugador2 = new Jugador("IA Minimax", 'O');
        } else {
            jugador1 = new Jugador("IA Minimax", 'X');
            jugador2 = new Jugador("Humano", 'O');
        }

        IA_Minimax ia = new IA_Minimax(fichaIA, fichaHumano);
        juego = new Juego(jugador1, jugador2, ia);

        juegoTerminado = false;
        limpiarTableroVisual();
        actualizarEstado();

        turnoDeLaIASiCorresponde();
    }

    /** Reinicia el tablero manteniendo la misma asignacion de fichas. */
    private void reiniciarPartida() {
        juego.reiniciar();
        juegoTerminado = false;
        limpiarTableroVisual();
        actualizarEstado();
        turnoDeLaIASiCorresponde();
    }

    private void manejarClicCasilla(int fila, int columna) {
        if (juegoTerminado) return;
        if (!esTurnoHumano()) return;

        if (!juego.realizarJugada(fila, columna)) {
            return; // casilla ocupada o jugada invalida
        }

        actualizarTableroVisual();

        if (verificarFinDeJuego()) return;

        juego.cambiarTurno();
        actualizarEstado();

        turnoDeLaIASiCorresponde();
    }

    /** Si le toca a la IA, calcula y ejecuta su jugada. */
    private void turnoDeLaIASiCorresponde() {
        if (juegoTerminado) return;
        if (esTurnoHumano()) return;

        etiquetaEstado.setText("La IA está pensando...");
        deshabilitarTablero();

        SwingWorker<int[], Void> worker = new SwingWorker<>() {
            @Override
            protected int[] doInBackground() {
                IA_Minimax ia = new IA_Minimax(fichaIA, fichaHumano);
                return ia.obtenerMejorMovimiento(juego.getTablero());
            }

            @Override
            protected void done() {
                try {
                    int[] movimiento = get();
                    if (movimiento[0] != -1) {
                        juego.realizarJugada(movimiento[0], movimiento[1]);
                        actualizarTableroVisual();
                        if (!verificarFinDeJuego()) {
                            juego.cambiarTurno();
                            actualizarEstado();
                            habilitarCasillasLibres();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private boolean esTurnoHumano() {
        return juego.getJugadorActual().getNombre().equals("Humano");
    }

    private void actualizarTableroVisual() {
        Color botonFondo = modoOscuro ? BOTON_OSCURO : BOTON_CLARO;
        Color texto = modoOscuro ? TEXTO_OSCURO : TEXTO_CLARO;

        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                char ficha = juego.getTablero().obtenerFicha(fila, columna);
                JButton boton = botonesCasillas[fila][columna];
                boton.setBackground(botonFondo);

                if (ficha == ' ') {
                    boton.setText("");
                    boton.setForeground(texto);
                    boton.setEnabled(esTurnoHumano());
                } else {
                    boton.setText(String.valueOf(ficha));
                    boton.setEnabled(false);
                    boton.setForeground(ficha == 'X'
                            ? new Color(60, 120, 220)
                            : new Color(220, 80, 60));
                }
            }
        }
    }

    /** Devuelve true si el juego termino (gano alguien o hubo empate) y actualiza la etiqueta de estado. */
    private boolean verificarFinDeJuego() {
        if (juego.jugadorGano()) {
            juegoTerminado = true;
            String ganador = juego.getJugadorActual().getNombre();
            etiquetaEstado.setForeground(new Color(0, 150, 0));
            etiquetaEstado.setText("¡Ganó " + ganador + " (" + juego.getJugadorActual().getFicha() + ")!");
            deshabilitarTablero();
            return true;
        } else if (juego.hayEmpate()) {
            juegoTerminado = true;
            etiquetaEstado.setForeground(new Color(200, 140, 0));
            etiquetaEstado.setText("¡Empate!");
            deshabilitarTablero();
            return true;
        }
        return false;
    }

    private void actualizarEstado() {
        if (!juegoTerminado) {
            etiquetaTurno.setText("Turno: " + juego.getJugadorActual().getNombre()
                    + " (" + juego.getJugadorActual().getFicha() + ")");
            etiquetaEstado.setForeground(modoOscuro ? TEXTO_OSCURO : TEXTO_CLARO);
            etiquetaEstado.setText(" ");
        }
    }

    private void deshabilitarTablero() {
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                botonesCasillas[fila][columna].setEnabled(false);
            }
        }
    }

    private void habilitarCasillasLibres() {
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                boolean vacia = juego.getTablero().obtenerFicha(fila, columna) == ' ';
                botonesCasillas[fila][columna].setEnabled(vacia && esTurnoHumano());
            }
        }
    }

    private void limpiarTableroVisual() {
        Color botonFondo = modoOscuro ? BOTON_OSCURO : BOTON_CLARO;
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                JButton boton = botonesCasillas[fila][columna];
                boton.setText("");
                boton.setEnabled(true);
                boton.setBackground(botonFondo);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazGrafica ventana = new InterfazGrafica();
            ventana.setVisible(true);
        });
    }
}
 