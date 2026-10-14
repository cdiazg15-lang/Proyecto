package com.mycompany.totito;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InterfazGrafica extends JFrame {

    private JButton[][] botonesCasillas = new JButton[3][3];
    private JLabel etiquetaTitulo;
    private JLabel etiquetaModo;
    private JLabel etiquetaTurno;
    private JLabel etiquetaEstado;
    private JButton botonNuevaPartida;
    private JButton botonReiniciar;
    private JButton botonVolverInicio;
    private JButton botonModoNoche;

    private PanelDegradado panelFondo;
    private JPanel panelTablero;
    private JPanel panelInferior;
    private ChipMarcador chipJugador1;
    private ChipMarcador chipEmpates;
    private ChipMarcador chipJugador2;

    private Juego juego;
    private Jugador jugador1;
    private Jugador jugador2;
    private char fichaHumano;
    private char fichaIA;
    private boolean modoVsIA = true;
    private boolean juegoTerminado = false;
    private boolean modoOscuro = false;
    private String dificultad = "Difícil";
    private int idPartida = 0;
    private final Random random = new Random();

    private int victoriasJugador1 = 0;
    private int victoriasJugador2 = 0;
    private int totalEmpates = 0;

    private static final Color ACENTO_AZUL = new Color(41, 98, 255);
    private static final Color ACENTO_ROJO = new Color(230, 70, 70);
    private static final Color ACENTO_DORADO = new Color(255, 190, 60);

    private static final Color GRAD_CLARO_1 = new Color(214, 231, 255);
    private static final Color GRAD_CLARO_2 = new Color(255, 255, 255);
    private static final Color GRAD_OSCURO_1 = new Color(18, 20, 34);
    private static final Color GRAD_OSCURO_2 = new Color(40, 32, 58);

    private static final Color BOTON_CLARO = Color.WHITE;
    private static final Color BOTON_OSCURO = new Color(55, 55, 70);
    private static final Color TEXTO_CLARO = new Color(30, 30, 40);
    private static final Color TEXTO_OSCURO = new Color(235, 235, 245);

    public InterfazGrafica() {
        super("Totito - IA Minimax Paulo");
        construirInterfaz();
        aplicarTema();
        iniciarNuevaPartida();
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        panelFondo = new PanelDegradado();
        panelFondo.setLayout(new BorderLayout(10, 10));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        setContentPane(panelFondo);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setOpaque(false);
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));

        JPanel filaTitulo = new JPanel(new BorderLayout());
        filaTitulo.setOpaque(false);

        etiquetaTitulo = new JLabel("TOTITO", SwingConstants.LEFT);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 30));

        botonModoNoche = crearBotonCircular(new LunaIcon(16));
        botonModoNoche.setToolTipText("Cambiar modo noche/dia");
        botonModoNoche.addActionListener(e -> alternarModoNoche());

        filaTitulo.add(etiquetaTitulo, BorderLayout.WEST);
        filaTitulo.add(botonModoNoche, BorderLayout.EAST);

        etiquetaModo = new JLabel(" ", SwingConstants.CENTER);
        etiquetaModo.setFont(new Font("Arial", Font.PLAIN, 13));
        etiquetaModo.setAlignmentX(Component.CENTER_ALIGNMENT);

        etiquetaTurno = new JLabel("Turno: -", SwingConstants.CENTER);
        etiquetaTurno.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaTurno.setAlignmentX(Component.CENTER_ALIGNMENT);

        etiquetaEstado = new JLabel(" ", SwingConstants.CENTER);
        etiquetaEstado.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaEstado.setAlignmentX(Component.CENTER_ALIGNMENT);

        chipJugador1 = new ChipMarcador("TÚ", ACENTO_AZUL);
        chipEmpates = new ChipMarcador("EMPATES", new Color(150, 150, 160));
        chipJugador2 = new ChipMarcador("BOT", ACENTO_ROJO);

        JPanel panelMarcador = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        panelMarcador.setOpaque(false);
        panelMarcador.add(chipJugador1);
        panelMarcador.add(chipEmpates);
        panelMarcador.add(chipJugador2);

        panelSuperior.add(filaTitulo);
        panelSuperior.add(etiquetaModo);
        panelSuperior.add(Box.createVerticalStrut(4));
        panelSuperior.add(etiquetaTurno);
        panelSuperior.add(etiquetaEstado);
        panelSuperior.add(panelMarcador);

        panelFondo.add(panelSuperior, BorderLayout.NORTH);

        panelTablero = new JPanel(new GridLayout(3, 3, 8, 8));
        panelTablero.setOpaque(false);
        panelTablero.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        Font fuenteBoton = new Font("Arial", Font.BOLD, 42);

        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                JButton boton = new JButton("");
                boton.setFont(fuenteBoton);
                boton.setFocusPainted(false);
                boton.setPreferredSize(new Dimension(100, 100));
                boton.setBorder(new LineBorder(ACENTO_AZUL, 2, true));
                agregarEfectoHover(boton);
                final int f = fila, c = columna;
                boton.addActionListener((ActionEvent e) -> manejarClicCasilla(f, c));
                botonesCasillas[fila][columna] = boton;
                panelTablero.add(boton);
            }
        }
        panelFondo.add(panelTablero, BorderLayout.CENTER);

        panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
        panelInferior.setOpaque(false);
        botonNuevaPartida = crearBotonAccion("Nueva partida", ACENTO_AZUL);
        botonReiniciar = crearBotonAccion("Reiniciar", ACENTO_DORADO);
        botonVolverInicio = crearBotonAccion("Volver al inicio", new Color(130, 130, 140));

        botonNuevaPartida.addActionListener(e -> iniciarNuevaPartida());
        botonReiniciar.addActionListener(e -> reiniciarPartida());
        botonVolverInicio.addActionListener(e -> volverAlInicio());

        panelInferior.add(botonNuevaPartida);
        panelInferior.add(botonReiniciar);
        panelInferior.add(botonVolverInicio);
        panelFondo.add(panelInferior, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JButton crearBotonAccion(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JButton crearBotonCircular(Icon icono) {
        JButton boton = new JButton(icono);
        boton.setPreferredSize(new Dimension(42, 42));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setBorder(BorderFactory.createEmptyBorder());
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void agregarEfectoHover(JButton boton) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (boton.isEnabled() && boton.getText().isEmpty()) {
                    boton.setBackground(modoOscuro
                            ? BOTON_OSCURO.brighter()
                            : new Color(232, 240, 255));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (boton.getText().isEmpty()) {
                    boton.setBackground(modoOscuro ? BOTON_OSCURO : BOTON_CLARO);
                }
            }
        });
    }

    private class PanelDegradado extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            Color colorArriba = modoOscuro ? GRAD_OSCURO_1 : GRAD_CLARO_1;
            Color colorAbajo = modoOscuro ? GRAD_OSCURO_2 : GRAD_CLARO_2;
            GradientPaint degradado = new GradientPaint(0, 0, colorArriba, 0, getHeight(), colorAbajo);
            g2.setPaint(degradado);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    private class ChipMarcador extends JPanel {
        private final JLabel etiquetaTexto;
        private String prefijo;
        private int valorActual;
        private Color colorFondo;

        ChipMarcador(String prefijo, Color colorFondo) {
            this.prefijo = prefijo;
            this.colorFondo = colorFondo;
            this.valorActual = 0;
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            etiquetaTexto = new JLabel(prefijo + ": 0");
            etiquetaTexto.setFont(new Font("Arial", Font.BOLD, 13));
            etiquetaTexto.setForeground(Color.WHITE);
            etiquetaTexto.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            add(etiquetaTexto);
        }

        void setValor(int valor) {
            this.valorActual = valor;
            etiquetaTexto.setText(prefijo + ": " + valor);
        }

        void setPrefijo(String nuevoPrefijo) {
            this.prefijo = nuevoPrefijo;
            etiquetaTexto.setText(prefijo + ": " + valorActual);
        }

        void setColorFondo(Color color) {
            this.colorFondo = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(colorFondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

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

    private void alternarModoNoche() {
        modoOscuro = !modoOscuro;
        aplicarTema();
    }

    private void aplicarTema() {
        Color botonFondo = modoOscuro ? BOTON_OSCURO : BOTON_CLARO;
        Color texto = modoOscuro ? TEXTO_OSCURO : TEXTO_CLARO;

        etiquetaTitulo.setForeground(modoOscuro ? ACENTO_DORADO : ACENTO_AZUL);
        etiquetaTurno.setForeground(texto);
        etiquetaModo.setForeground(texto);
        if (!juegoTerminado) {
            etiquetaEstado.setForeground(texto);
        }

        botonModoNoche.setIcon(modoOscuro ? new SolIcon(16) : new LunaIcon(16));

        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                JButton boton = botonesCasillas[fila][columna];
                boton.setBackground(botonFondo);
                if (boton.getText().isEmpty()) {
                    boton.setForeground(texto);
                }
            }
        }

        panelFondo.repaint();
    }

    private void iniciarNuevaPartida() {
        String[] modos = {"Contra la IA", "1 vs 1"};
        int modoSeleccionado = JOptionPane.showOptionDialog(
                this,
                "¿Cómo quieres jugar?",
                "Nueva partida",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                modos,
                modos[0]
        );
        modoVsIA = (modoSeleccionado != 1);

        if (modoVsIA) {
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

            String[] niveles = {"Fácil", "Medio", "Difícil"};
            int nivel = JOptionPane.showOptionDialog(
                    this,
                    "Elige la dificultad del bot",
                    "Dificultad",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    niveles,
                    niveles[2]
            );
            dificultad = (nivel >= 0) ? niveles[nivel] : "Difícil";
            etiquetaModo.setText("Contra la IA - " + dificultad);

            if (fichaHumano == 'X') {
                jugador1 = new Jugador("Humano", 'X');
                jugador2 = new Jugador("IA Minimax", 'O');
            } else {
                jugador1 = new Jugador("IA Minimax", 'X');
                jugador2 = new Jugador("Humano", 'O');
            }

            chipJugador1.setPrefijo("TÚ");
            chipJugador2.setPrefijo("BOT");
        } else {
            String[] opciones = {"X", "O"};
            int seleccion = JOptionPane.showOptionDialog(
                    this,
                    "Jugador 1, ¿con qué ficha quieres jugar?",
                    "Nueva partida",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            fichaHumano = 'X';
            fichaIA = 'O';

            if (seleccion == 1) {
                jugador1 = new Jugador("Jugador 2", 'X');
                jugador2 = new Jugador("Jugador 1", 'O');
            } else {
                jugador1 = new Jugador("Jugador 1", 'X');
                jugador2 = new Jugador("Jugador 2", 'O');
            }

            etiquetaModo.setText("Modo 1 vs 1");
            chipJugador1.setPrefijo("J1");
            chipJugador2.setPrefijo("J2");
        }

        IA_Minimax ia = new IA_Minimax(fichaIA, fichaHumano);
        juego = new Juego(jugador1, jugador2, ia);

        idPartida++;
        juegoTerminado = false;
        limpiarTableroVisual();
        actualizarEstado();

        turnoDeLaIASiCorresponde();
    }

    private void volverAlInicio() {
        victoriasJugador1 = 0;
        victoriasJugador2 = 0;
        totalEmpates = 0;
        actualizarMarcador();
        iniciarNuevaPartida();
    }

    private void reiniciarPartida() {
        juego.reiniciar();
        idPartida++;
        juegoTerminado = false;
        limpiarTableroVisual();
        actualizarEstado();
        turnoDeLaIASiCorresponde();
    }

    private void manejarClicCasilla(int fila, int columna) {
        if (juegoTerminado) return;
        if (esTurnoDeIA()) return;

        if (!juego.realizarJugada(fila, columna)) {
            return;
        }

        actualizarTableroVisual();

        if (verificarFinDeJuego()) return;

        juego.cambiarTurno();
        actualizarEstado();

        turnoDeLaIASiCorresponde();
    }

    private void turnoDeLaIASiCorresponde() {
        if (juegoTerminado) return;
        if (!esTurnoDeIA()) return;

        etiquetaEstado.setText("La IA está pensando...");
        deshabilitarTablero();

        final int idActual = idPartida;
        SwingWorker<int[], Void> worker = new SwingWorker<>() {
            @Override
            protected int[] doInBackground() throws Exception {
                Thread.sleep(500);
                return calcularMovimientoIA();
            }

            @Override
            protected void done() {
                try {
                    if (idActual != idPartida || juegoTerminado) return;
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

    private int[] calcularMovimientoIA() {
        double probabilidadAzar;
        switch (dificultad) {
            case "Fácil":
                probabilidadAzar = 0.75;
                break;
            case "Medio":
                probabilidadAzar = 0.35;
                break;
            default:
                probabilidadAzar = 0.0;
                break;
        }

        if (random.nextDouble() < probabilidadAzar) {
            List<int[]> libres = new ArrayList<>();
            for (int fila = 0; fila < 3; fila++) {
                for (int columna = 0; columna < 3; columna++) {
                    if (juego.getTablero().casillaDisponible(fila, columna)) {
                        libres.add(new int[]{fila, columna});
                    }
                }
            }
            if (!libres.isEmpty()) {
                return libres.get(random.nextInt(libres.size()));
            }
        }

        IA_Minimax ia = new IA_Minimax(fichaIA, fichaHumano);
        return ia.obtenerMejorMovimiento(juego.getTablero());
    }

    private void resaltarLineaGanadora(char ficha) {
        int[][][] lineas = {
            {{0, 0}, {0, 1}, {0, 2}}, {{1, 0}, {1, 1}, {1, 2}}, {{2, 0}, {2, 1}, {2, 2}},
            {{0, 0}, {1, 0}, {2, 0}}, {{0, 1}, {1, 1}, {2, 1}}, {{0, 2}, {1, 2}, {2, 2}},
            {{0, 0}, {1, 1}, {2, 2}}, {{0, 2}, {1, 1}, {2, 0}}
        };
        Color verde = modoOscuro ? new Color(30, 110, 70) : new Color(170, 235, 195);
        for (int[][] linea : lineas) {
            boolean completa = true;
            for (int[] celda : linea) {
                if (juego.getTablero().obtenerFicha(celda[0], celda[1]) != ficha) {
                    completa = false;
                }
            }
            if (completa) {
                for (int[] celda : linea) {
                    botonesCasillas[celda[0]][celda[1]].setBackground(verde);
                }
            }
        }
    }

    private boolean esTurnoDeIA() {
        return modoVsIA && juego.getJugadorActual().getNombre().equals("IA Minimax");
    }

    private void actualizarTableroVisual() {
        Color botonFondo = modoOscuro ? BOTON_OSCURO : BOTON_CLARO;
        Color texto = modoOscuro ? TEXTO_OSCURO : TEXTO_CLARO;
        boolean sePuedeJugar = !esTurnoDeIA();

        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                char ficha = juego.getTablero().obtenerFicha(fila, columna);
                JButton boton = botonesCasillas[fila][columna];
                boton.setBackground(botonFondo);

                if (ficha == ' ') {
                    boton.setText("");
                    boton.setForeground(texto);
                    boton.setEnabled(sePuedeJugar);
                } else {
                    boton.setText(String.valueOf(ficha));
                    boton.setEnabled(false);
                    boton.setForeground(ficha == 'X' ? ACENTO_AZUL : ACENTO_ROJO);
                }
            }
        }
    }

    private boolean verificarFinDeJuego() {
        if (juego.jugadorGano()) {
            juegoTerminado = true;
            Jugador ganador = juego.getJugadorActual();

            String nombreGanador = ganador.getNombre();
            if (nombreGanador.equals("Humano") || nombreGanador.equals("Jugador 1")) {
                victoriasJugador1++;
            } else {
                victoriasJugador2++;
            }
            actualizarMarcador();

            etiquetaEstado.setForeground(new Color(30, 160, 90));
            etiquetaEstado.setText("¡Ganó " + ganador.getNombre() + " (" + ganador.getFicha() + ")!");
            deshabilitarTablero();
            resaltarLineaGanadora(ganador.getFicha());
            return true;
        } else if (juego.hayEmpate()) {
            juegoTerminado = true;
            totalEmpates++;
            actualizarMarcador();

            etiquetaEstado.setForeground(ACENTO_DORADO.darker());
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

    private void actualizarMarcador() {
        chipJugador1.setValor(victoriasJugador1);
        chipEmpates.setValor(totalEmpates);
        chipJugador2.setValor(victoriasJugador2);
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
                botonesCasillas[fila][columna].setEnabled(vacia && !esTurnoDeIA());
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
