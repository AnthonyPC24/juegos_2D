package Juego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class JuegoPanel extends JPanel implements ActionListener, KeyListener {
    private static final int PERSONAJE_ANCHO = 80;
    private static final int PERSONAJE_ALTO = 80;
    private final Timer timer;
    private final Timer cronometro;
    private final Personaje personaje;

    private final Image imagenMoneda;
    private final Image imagenEstrella;
    private final Image imagenPersonaje;

    private final ArrayList<Plataforma> plataformas;
    private final ArrayList<Moneda> monedas;

    // Comentar para no olvidarme
    private int x = 100, y = 500; // Posición del personaje
    private int velocidadY = 0;
    private int puntos = 0;
    private int monedasRecolectadas = 0;
    private int bonusAcumulado = 0;
    private boolean partidaFinalizada = false;
    private long tiempoInicio;
    int estrellaX = 450;
    int estrellaY = 100;
    int estrellaAncho = 60;
    int estrellaAlto = 60;



    public JuegoPanel(Personaje personaje) {
        this.personaje = personaje;
        setBackground(new Color(217, 217, 214));
        setFocusable(true);
        addKeyListener(this);

        //Ruta imagen de monedas
        this.imagenMoneda = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagen/moneda.gif"))).getImage();
        if (imagenMoneda == null) {
            System.out.println("Imagen de moneda no encontrada");
        }
        // ruta imagen estrella
        this.imagenEstrella = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagen/estrella.gif"))).getImage();
        if (imagenEstrella == null) {
            System.out.println("Imagen de estrella no encontrada");
        }

        // Usa solo una imagen para el personaje
        this.imagenPersonaje = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagen/personaje.gif"))).getImage();

        tiempoInicio = System.currentTimeMillis();

        plataformas = new ArrayList<>();
        generarPlataformasAleatorias(6);

        monedas = new ArrayList<>();
        generarMonedasAleatorias();

        timer = new Timer(20, this);
        timer.start();

        cronometro = new Timer(1000, e -> verificarTiempo());
        cronometro.start();

    }


    private void generarPlataformasAleatorias(int cantidad) {
        plataformas.clear();

        plataformas.add(new Plataforma(0, 560, 800, 40)); // Suelo
        plataformas.add(new Plataforma(460, 180, 60, 20));

        Random random = new Random();
        int y = 440;

        for (int i = 0; i < cantidad; i++) {
            int x = random.nextInt(650);
            int width = 60;
            int height = 20;

            plataformas.add(new Plataforma(x, y, width, height));
            y -= 60 + random.nextInt(20);
        }
    }

    private void generarMonedasAleatorias() {
        monedas.clear();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            // Elegimos una plataforma aleatoria que no sea el suelo (índice 0)
            int index = 1 + random.nextInt(plataformas.size() - 1);
            Plataforma p = plataformas.get(index);

            // Centrar la moneda encima de la plataforma
            int monedaX = p.x + (p.width - Moneda.ANCHO) / 2;
            int monedaY = p.y - Moneda.ALTO; // Justo encima

            monedas.add(new Moneda(monedaX, monedaY));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Personaje
        g.drawImage(imagenPersonaje, x, y, PERSONAJE_ANCHO, PERSONAJE_ALTO, this);

        // Plataformas
        g.setColor(new Color(154, 221, 225));
        for (Plataforma p : plataformas) {
            g.fillRect(p.x, p.y, p.width, p.height);
        }

        // Monedas
        for (Moneda m : monedas) {
            g.drawImage(imagenMoneda, m.getX(), m.getY(), 50, 50, this);
        }

        // Estrella (objetivo final)
        g.drawImage(imagenEstrella, estrellaX, estrellaY, estrellaAncho, estrellaAlto, this);

        //Fuentes de la letra y tamaño
        g.setFont(new Font("Poppins", Font.BOLD, 15));
        g.setColor(new Color(0, 163, 224));

        // Puntos
        g.drawString("Puntos: " + puntos, 20, 20);

        // Mostrar el cronómetro
        long tiempoTranscurrido = (System.currentTimeMillis() - tiempoInicio) / 1000;
        g.drawString("Tiempo: " + tiempoTranscurrido + "s", 20, 40);

        // Mostrar los puntos totales
        int puntosTotales = puntos + bonusAcumulado; // Sumar puntos y bonus
        g.drawString("Puntos Totales: " + puntosTotales, 20, 60);  // Muestra los puntos totales
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (partidaFinalizada) return;

        velocidadY += 1;
        y += velocidadY;

        for (Plataforma p : plataformas) {
            Rectangle rPersonaje = new Rectangle(x, y, PERSONAJE_ALTO, PERSONAJE_ANCHO); //
            if (rPersonaje.intersects(p.getBounds()) && velocidadY > 0) {
                y = p.y - PERSONAJE_ALTO; // lo colocamos justo encima de la plataforma
                velocidadY = 0;
                break;
            }
        }
        // Colisión con monedas
        for (int i = 0; i < monedas.size(); i++) {
            Moneda m = monedas.get(i);
            Rectangle rPersonaje = new Rectangle(x + 20, y + 50, 40, 30);
            if (rPersonaje.intersects(m.getBounds())) {
                int bonus = 0;

                // Verificar el tipo de personaje y asignar bonus
                if (personaje.getTipo().equals("Principiante")) {
                    bonus = 10;  // Bonus para Principiante
                } else if (personaje.getTipo().equals("Escalador")) {
                    bonus = 5;   // Bonus para Escalador
                }

                puntos += 5 + bonus;  // Sumar puntos por la moneda y el bonus

                monedasRecolectadas++;
                monedas.remove(i);
                break;
            }
        }

        // Colisión con la estrella
        Rectangle personajeRect = new Rectangle(x, y, PERSONAJE_ALTO, PERSONAJE_ANCHO);
        Rectangle estrellaRect = new Rectangle(estrellaX, estrellaY, estrellaAncho, estrellaAlto);
        if (personajeRect.intersects(estrellaRect)) {
            finalizarPartida("estrella");
        }

        repaint();
    }

    private void verificarTiempo() {
        long tiempoTranscurrido = (System.currentTimeMillis() - tiempoInicio) / 1000;
        if (tiempoTranscurrido >= 30 && !partidaFinalizada) {
            finalizarPartida("tiempo");
        }
        repaint();  // Actualizar el tiempo en pantalla
    }


    private void finalizarPartida(String motivo) {
        partidaFinalizada = true;
        timer.stop();
        cronometro.stop();

        // Calcular los puntos
        int puntosPorMonedas = monedasRecolectadas * 5;  // 5 puntos por moneda
        int puntosPorEstrella = 0;

        if (motivo.equals("estrella")) {
            puntosPorEstrella = 50;
        } // 50 puntos por estrella

        // Calcular los puntos totales
        int total = puntos + puntosPorEstrella;

        // Guardar el puntaje en la base de datos
        int idPersonaje = personaje.getId();  // Obtener el id del personaje
        GestorBD.guardarPuntaje(idPersonaje, puntosPorMonedas, bonusAcumulado, puntosPorEstrella, total);

        // Mostrar el mensaje final
        JOptionPane.showMessageDialog(this, "¡Partida finalizada por " + motivo + "! Puntos Totales: " + total);

        // Preguntar si el jugador quiere jugar otra vez
        int respuesta = JOptionPane.showOptionDialog(this, "¿Quieres jugar otra vez?", "Fin del juego",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

        if (respuesta == JOptionPane.YES_OPTION) {
            reiniciarJuego();
        } else {
            System.exit(0);  // Salir del juego si el jugador no quiere continuar
        }
    }

    private void reiniciarJuego() {
        // Reiniciar las variables y objetos necesarios para volver a empezar
        x = 100;
        y = 500;
        velocidadY = 0;
        puntos = 0;
        monedasRecolectadas = 0;
        bonusAcumulado = 0;
        tiempoInicio = System.currentTimeMillis();

        // Reiniciar plataformas
        generarPlataformasAleatorias(5);

        // Reiniciar monedas
        monedas.clear();
        generarMonedasAleatorias();

        partidaFinalizada = false;  // Volver a permitir que el jugador juegue
        timer.start();              // Reiniciar el temporizador
        cronometro.start();         // Reiniciar el cronómetro
        repaint();                  // Volver a pintar el panel para actualizar la interfaz
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (partidaFinalizada) return;

        int salto = personaje.getSalto();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> x -= 20;
            case KeyEvent.VK_RIGHT -> x += 20;
            case KeyEvent.VK_UP -> {
                if (velocidadY == 0) {
                    velocidadY = -salto;
                }
            }
        }
    }


    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}


