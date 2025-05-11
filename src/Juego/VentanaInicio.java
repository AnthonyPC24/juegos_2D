package Juego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaInicio extends JFrame {
    private JTextField nombreTextField;
    private JComboBox<String> tipoComboBox;
    private JButton iniciarButton;

    public VentanaInicio() {
        setTitle("The Star");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JLabel nombreLabel = new JLabel("Introduce tu nombre: ");
        nombreTextField = new JTextField(20);

        JLabel tipoLabel = new JLabel("Selecciona tu tipo: ");
        tipoComboBox = new JComboBox<>(new String[]{"Principiante", "Escalador"});

        iniciarButton = new JButton("Iniciar Juego");

        add(nombreLabel);
        add(nombreTextField);
        add(tipoLabel);
        add(tipoComboBox);
        add(iniciarButton);

        iniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreTextField.getText();
                String tipo = (String) tipoComboBox.getSelectedItem();

                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa tu nombre");
                } else {
                    Personaje personaje;

                    if (tipo.equals("Principiante")) {
                        personaje = new Principiante(nombre);
                    } else {
                        personaje = new Escalador(nombre);
                    }

                    // Insertamos el personaje en la base de datos y obtenemos su ID
                    personaje = GestorBD.insertarPersonaje(personaje);

                    if (personaje.getId() != -1) { // Verificar que se haya insertado correctamente
                        JOptionPane.showMessageDialog(null, "¡Juego Iniciado! Personaje: " + personaje.getNombre() + " Tipo: " + personaje.getTipo());
                        dispose(); // Cerrar la ventana de inicio

                        // Abrir la ventana del juego con el personaje
                        new VentanaJuego(personaje);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al crear el personaje en la base de datos.");
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaInicio().setVisible(true);
            }
        });
    }
}
