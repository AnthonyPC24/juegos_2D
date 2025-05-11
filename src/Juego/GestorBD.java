package Juego;

import java.sql.*;

public class GestorBD {
    private static final String db_url = "jdbc:mysql://localhost:3306/juego_2d";
    private static final String db_user = "root";
    private static final String db_pass = "mysql";

    public static Connection conectar() {
        try {
            Connection con = DriverManager.getConnection(db_url, db_user, db_pass);
            System.out.println("Conexión exitosa a la base de datos.");
            return con;
        } catch (SQLException e) {
            System.out.println("La conexión de datos ha fallado: " + e.getMessage());
            return null;
        }
    }

    // Guardar puntajes
    // Guardar puntajes con referencia a id_personaje
    public static void guardarPuntaje(int idPersonaje, int puntosPorMonedas, int bonusAdicional, int puntosPorEstrella, int total) {
        String sql = "INSERT INTO puntajes (id_personaje, por_moneda, bonus_adicional, estrella, total) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPersonaje);  // Asignamos el id_personaje
            ps.setInt(2, puntosPorMonedas);
            ps.setInt(3, bonusAdicional);
            ps.setInt(4, puntosPorEstrella);
            ps.setInt(5, total);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al guardar el puntaje: " + e.getMessage());
        }
    }


    // Insertar personaje
    public static Personaje insertarPersonaje(Personaje personaje) {
        String sql = "SELECT tipo_id FROM tipos_personaje WHERE categoria = ?";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, personaje.getTipo());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int tipoId = rs.getInt("tipo_id");

                String insertSql = "INSERT INTO personajes (nombre, tipo_id) VALUES (?, ?)";
                try (PreparedStatement insertPs = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    insertPs.setString(1, personaje.getNombre());
                    insertPs.setInt(2, tipoId);

                    int filas = insertPs.executeUpdate();
                    if (filas > 0) {
                        ResultSet generatedKeys = insertPs.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int idGenerado = generatedKeys.getInt(1);
                            personaje.setId(idGenerado);
                        }
                    }
                }
            } else {
                System.out.println("Tipo de personaje no encontrado en la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar personaje: " + e.getMessage());
        }

        return personaje;
    }
}



