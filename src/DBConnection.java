import java.sql.*;

public class DBConnection {
    private static String URL = "jdbc:mysql://localhost:3306/ftms";
    private static String USER = "root";
    private static String PASSWORD = "";

    public static Connection getConnection()
    {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database Connected Successfully!");
        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
        }
        return connection;
    }
}
