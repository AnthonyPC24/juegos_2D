package Juego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaInicio extends JFrame {
    private JTextField nombreTextField;
    private JComboBox<String> tipoComboBox;
    private JButton iniciarButton;

    public VentanaInicio() {

        Toolkit pantalla = Toolkit.getDefaultToolkit();
        Image icon = pantalla.getImage("src/imagen/politecnics.png");
        setIconImage(icon);

        setTitle("The Star");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        //Color de fondo de la ventana
        getContentPane().setBackground(new Color(0, 163, 224));

        // Crear los componentes de la ventana
        crearComponentes();

        // Añadir el listener al botón de iniciar
        iniciarButton.addActionListener(this::iniciarJuego);
    }

    private void crearComponentes() {
        JLabel nombreLabel = new JLabel("Introduce tu nombre: ");
        nombreTextField = new JTextField(20);

        JLabel tipoLabel = new JLabel("Selecciona tu tipo: ");
        tipoComboBox = new JComboBox<>(new String[]{"Principiante", "Escalador"});

        iniciarButton = new JButton("Iniciar Juego");

        // Añadir los componentes a la ventana
        add(nombreLabel);
        add(nombreTextField);
        add(tipoLabel);
        add(tipoComboBox);
        add(iniciarButton);
    }

    private void iniciarJuego(ActionEvent e) {
        String nombre = nombreTextField.getText();
        String tipo = (String) tipoComboBox.getSelectedItem();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa tu nombre");
        } else {
            Personaje personaje = crearPersonaje(nombre, tipo);
            if (personaje != null) {
                // Insertamos el personaje en la base de datos y obtenemos su ID
                personaje = GestorBD.insertarPersonaje(personaje);

                if (personaje.getId() != -1) { // Verificar que se haya insertado correctamente
                    JOptionPane.showMessageDialog(this, "¡Juego Iniciado! Personaje: " + personaje.getNombre() + " Tipo: " + personaje.getTipo());
                    dispose(); // Cerrar la ventana de inicio

                    // Abrir la ventana del juego con el personaje
                    new VentanaJuego(personaje);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear el personaje en la base de datos.");
                }
            }
        }
    }

    private Personaje crearPersonaje(String nombre, String tipo) {
        if (tipo.equals("Principiante")) {
            return new Principiante(nombre);
        } else if (tipo.equals("Escalador")) {
            return new Escalador(nombre);
        } else {
            return null; // Si el tipo no es reconocido
        }
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

