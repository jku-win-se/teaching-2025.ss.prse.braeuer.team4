package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection_DatabaseDKE {
    private static final String URL="jdbc:oracle:thin:@//localhost:1521/FREEPDB1?user=system&password=root";


    //TODO check if it works.

    public static Connection connectToDBDKE() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Verbindung zu DKE Datenbank erfolgreich!");
        } catch (SQLException e) {
            System.out.println("Fehler bei der Verbindung: " + e.getMessage());
        }
        return conn;
    }
    public static void main(String[] args) {
        connectToDBDKE();
    }
}
