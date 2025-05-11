import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static final String db_url ="jdbc:mysql://localhost:3306/juego_2d";
    private static final String db_user = "root";
    private static final String db_pass = "mysql";

    public static void main(String[] args) {

        try{
            Connection con = DriverManager.getConnection(db_url,db_user, db_pass);
            con.close();
        } catch (SQLException e) {
            System.out.println("La conexion de datos a fallado");
        }


    }
}