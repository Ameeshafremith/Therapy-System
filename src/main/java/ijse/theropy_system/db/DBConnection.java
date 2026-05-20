package ijse.theropy_system.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String DB_URL ="jdbc:mysql://localhost:3306/nilmini_stitch_studio";
    private final String DB_USERNAME ="root";
    private final String DB_PASSWORD ="1234";
    
    private final Connection connection;
    private static DBConnection dbc;
    
    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
    }
    public static DBConnection getInstance() throws SQLException {
        return (dbc==null) ? dbc=new DBConnection() :dbc; 
        
    }
     public Connection getConnection(){
         return connection;
     }
    
}
