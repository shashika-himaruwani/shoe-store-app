package lk.sh.shoesstoreapp.db;





import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    //reference variable default value = null
    private static DBConnection dbConnection;

    private final Connection connection;

    //constructor
    private DBConnection() throws ClassNotFoundException, SQLException {
        // Load driver class to ram
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish a connection to the MySQL database.
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shoestore", "root", "2001");
    }

    public static DBConnection getDBConnection() throws SQLException, ClassNotFoundException {
        if(dbConnection == null ) {
            //object ek cl krddi constructor ek prk cl wenew
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }


    public Connection getConnection() {
        return connection;
    }

}


