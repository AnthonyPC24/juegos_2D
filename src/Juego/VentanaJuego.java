package Juego;

import javax.swing.*;

public class VentanaJuego extends JFrame {

    public VentanaJuego(Personaje personaje) {
        setTitle("Juego Plataforma The Star");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JuegoPanel panel = new JuegoPanel(personaje);
        add(panel);

        setVisible(true);
        panel.requestFocusInWindow();
    }
}
