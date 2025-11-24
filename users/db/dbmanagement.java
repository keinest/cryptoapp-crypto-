package crypto.users.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import crypto.users.User;

public class dbmanagement 
{
    private static final String DB_URL = "jdbc:mysql://localhost:5432/crypto_app";

    public static Connection connect() 
    {
        Connection conn = null;
        String username = "root";           
        String password = " "; 
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, username, password);
            return conn;
        } 
        
        catch(SQLException | ClassNotFoundException e)
        {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
            return null;
        }
    }
   
    public static void createTable() 
    {
        String sql = "CREATE TABLE IF NOT EXISTS members ("
                + "id INT PRIMARY KEY AUTO_INCREMENT,"
                + "login VARCHAR(255) NOT NULL UNIQUE,"
                + "email VARCHAR(255) NOT NULL UNIQUE,"
                + "password VARCHAR(255) NOT NULL"
                + ");";

        try(Connection conn = connect();PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            if(pstmt != null)
                pstmt.execute();
        } 

        catch(SQLException e) {System.err.println("Erreur lors de la création de la table : " + e.getMessage());}
    }

    public static boolean registerUser(User user) 
    {
        String sql = "INSERT INTO members(login, email, password) VALUES(?, ?, ?)";
        try(Connection conn = connect();PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            if(pstmt != null) 
            {
                pstmt.setString(1, user.getLogin());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getPassword());
                pstmt.executeUpdate();
                return true;
            }
        } 
        catch(SQLException e){System.err.println("Erreur lors de l'enregistrement de l'utilisateur : " + e.getMessage());}
        return false;
    }

    public static boolean verifyUser(String login, String password) 
    {
        String sql = "SELECT * FROM members WHERE login = ? AND password = ?";
        try(Connection conn = connect();PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            if (pstmt != null) 
            {
                pstmt.setString(1, login);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()) 
                    return true;
            }
        } 
        catch (SQLException e) {System.err.println("Erreur lors de la vérification de l'utilisateur : " + e.getMessage());}
        return false;
    }

    public static boolean userExists(String login, String email) 
    {
        String sql = "SELECT * FROM members WHERE login = ? OR email = ?";
        try(Connection conn = connect();PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            if(pstmt != null) 
            {
                pstmt.setString(1, login);
                pstmt.setString(2, email);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next())
                    return true;
            }
        } 
        catch (SQLException e){System.err.println("Erreur lors de la vérification de l'existence de l'utilisateur : " + e.getMessage());}
        return false;
    }
}
