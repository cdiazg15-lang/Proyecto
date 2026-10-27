
package com.mycompany.totito;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private final Color fondo = new Color(8, 13, 27);
    private final Color turquesa = new Color(0, 245, 212);
    private final Color rosa = new Color(255, 79, 216);

    public MenuPrincipal() {
        setTitle("Totito - IA Minimax Paulo");
        setSize(520, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel principal = new JPanel();
        principal.setBackground(fondo);
        principal.setBorder(
            BorderFactory.createEmptyBorder(35, 45, 30, 45)
        );
        principal.setLayout(
            new BoxLayout(principal, BoxLayout.Y_AXIS)
        );

        JLabel etiqueta = new JLabel("ARCADE");
        etiqueta.setFont(new Font("Arial", Font.BOLD, 16));
        etiqueta.setForeground(turquesa);
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("TOTITO");
        titulo.setFont(new Font("Arial", Font.BOLD, 54));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("IA MINIMAX");
        subtitulo.setFont(new Font("Arial", Font.BOLD, 16));
        subtitulo.setForeground(rosa);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        principal.add(etiqueta);
        principal.add(Box.createVerticalStrut(12));
        principal.add(titulo);
        principal.add(subtitulo);
        principal.add(Box.createVerticalStrut(30));

        JPanel tablero = new JPanel(new GridLayout(3, 3, 8, 8));
        tablero.setOpaque(false);
        tablero.setMaximumSize(new Dimension(240, 240));
        tablero.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] fichas = {
            "X", "", "O",
            "", "X", "",
            "O", "", "X"
        };

        for (String ficha : fichas) {
            JLabel celda = new JLabel(
                ficha.isEmpty() ? "·" : ficha,
                SwingConstants.CENTER
            );

            celda.setOpaque(true);
            celda.setBackground(new Color(17, 29, 51));
            celda.setFont(new Font("Arial", Font.BOLD, 38));

            celda.setForeground(
                ficha.equals("X") ? turquesa :
                ficha.equals("O") ? rosa :
                new Color(55, 75, 105)
            );

            celda.setBorder(
                BorderFactory.createLineBorder(
                    new Color(38, 57, 86), 1
                )
            );

            tablero.add(celda);
        }

        principal.add(tablero);
        principal.add(Box.createVerticalStrut(30));

        JButton jugarIA = crearBoton(
            "JUGAR CONTRA LA IA", turquesa
        );

        JButton dosJugadores = crearBoton(
            "DOS JUGADORES", rosa
        );

        JButton salir = crearBoton(
            "SALIR", new Color(100, 120, 150)
        );

        principal.add(jugarIA);
        principal.add(Box.createVerticalStrut(12));
        principal.add(dosJugadores);
        principal.add(Box.createVerticalStrut(12));
        principal.add(salir);

        jugarIA.addActionListener(e -> abrirJuego());

        dosJugadores.addActionListener(e -> {
            abrirJuego();
        });

        salir.addActionListener(e -> System.exit(0));

        add(principal);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);

        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setForeground(color);
        boton.setBackground(new Color(17, 29, 51));
        boton.setFocusPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);

        boton.setMaximumSize(new Dimension(330, 48));
        boton.setPreferredSize(new Dimension(330, 48));

        boton.setBorder(
            BorderFactory.createLineBorder(color, 2)
        );

        boton.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        return boton;
    }

    private void abrirJuego() {
        dispose();

        SwingUtilities.invokeLater(() -> {
            InterfazGrafica ventana = new InterfazGrafica();
            ventana.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}
