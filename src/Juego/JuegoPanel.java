package Juego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class JuegoPanel extends JPanel implements ActionListener, KeyListener {
    // Comentar para no olvidarme
    private Personaje personaje;
    private int x = 100, y = 500;
    private int velocidadY = 0;
    private Timer timer, cronometro;
    private ArrayList<Plataforma> plataformas;
    private ArrayList<Moneda> monedas;
    private int puntos = 0;
    private int monedasRecolectadas = 0;
    private int bonusAcumulado = 0;
    private boolean partidaFinalizada = false;
    private long tiempoInicio;

    public JuegoPanel(Personaje personaje) {
        this.personaje = personaje;
        setBackground(Color.CYAN);
        setFocusable(true);
        addKeyListener(this);

        tiempoInicio = System.currentTimeMillis();

        plataformas = new ArrayList<>();
        plataformas.add(new Plataforma(100, 540, 100, 10)); // suelo
        plataformas.add(new Plataforma(200, 450, 100, 10));
        plataformas.add(new Plataforma(300, 350, 100, 10));
        plataformas.add(new Plataforma(400, 250, 100, 10));
        plataformas.add(new Plataforma(500, 150, 100, 10));

        monedas = new ArrayList<>();
        generarMonedasAleatorias(5);

        timer = new Timer(20, this);
        timer.start();

        cronometro = new Timer(1000, e -> verificarTiempo());
        cronometro.start();
    }

    private void generarMonedasAleatorias(int cantidad) {
        Random random = new Random();
        for (int i = 0; i < cantidad; i++) {
            int x = random.nextInt(700);
            int y = 100 + random.nextInt(400);
            monedas.add(new Moneda(x, y));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Personaje
        g.setColor(Color.red);
        g.fillOval(x, y, 40, 40);

        // Plataformas
        g.setColor(Color.green);
        for (Plataforma p : plataformas) {
            g.fillRect(p.x, p.y, p.width, p.height);
        }

        // Monedas
        g.setColor(Color.yellow);
        for (Moneda m : monedas) {
            g.fillOval(m.getX(), m.getY(), 20, 20);
        }

        // Estrella (objetivo final)
        g.setColor(Color.orange);
        g.fillOval(450, 100, 30, 30);

        // Puntos
        g.setColor(Color.black);
        g.drawString("Puntos: " + puntos, 20, 20);

        // Mostrar el cronómetro
        long tiempoTranscurrido = (System.currentTimeMillis() - tiempoInicio) / 1000;
        g.setColor(Color.BLACK);
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
            Rectangle rPersonaje = new Rectangle(x, y, 40, 40);
            if (rPersonaje.intersects(p.getBounds()) && velocidadY > 0) {
                y = p.y - 40;
                velocidadY = 0;
                break;
            }
        }

        // Colisión con monedas
        for (int i = 0; i < monedas.size(); i++) {
            Moneda m = monedas.get(i);
            Rectangle rPersonaje = new Rectangle(x, y, 40, 40);
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
        Rectangle personajeRect = new Rectangle(x, y, 40, 40);
        Rectangle estrellaRect = new Rectangle(500, 100, 30, 30);
        if (personajeRect.intersects(estrellaRect)) {
            finalizarPartida("estrella");
        }

        repaint();
    }

    private void verificarTiempo() {
        long tiempoTranscurrido = (System.currentTimeMillis() - tiempoInicio) / 1000;
        System.out.println("Tiempo transcurrido: " + tiempoTranscurrido);
        if (tiempoTranscurrido >= 60 && !partidaFinalizada) {
            finalizarPartida("tiempo");
        }
    }


    private void finalizarPartida(String motivo) {
        partidaFinalizada = true;
        timer.stop();
        cronometro.stop();

        // Calcular los puntos
        int puntosPorMonedas = monedasRecolectadas * 5;  // 5 puntos por moneda
        int puntosPorEstrella = motivo.equals("estrella") ? 50 : 0;  // 50 puntos por estrella

        // Calcular los puntos totales
        int total = puntos + puntosPorEstrella;  // No necesitas bonusAcumulado porque ya lo sumas en el cálculo de las monedas

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




    // Método para reiniciar el juego
    private void reiniciarJuego() {
        // Reiniciar las variables y objetos necesarios para volver a empezar
        x = 100;
        y = 500;
        velocidadY = 0;
        puntos = 0;
        monedasRecolectadas = 0;
        bonusAcumulado = 0;
        tiempoInicio = System.currentTimeMillis();
        // Asegúrate de agregar plataformas y monedas nuevamente
        plataformas.clear();
        monedas.clear();
        generarMonedasAleatorias(5);  // Genera las monedas nuevamente
        partidaFinalizada = false;  // Volver a permitir que el jugador juegue
        timer.start();  // Reiniciar el temporizador
        cronometro.start();  // Reiniciar el cronómetro
        repaint();  // Volver a pintar el panel para actualizar la interfaz
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (partidaFinalizada) return;

        int salto = personaje.getSalto();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> x -= 15;
            case KeyEvent.VK_RIGHT -> x += 15;
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


