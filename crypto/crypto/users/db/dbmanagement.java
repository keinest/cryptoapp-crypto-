package crypto.users.db;

import java.sql.ResultSet;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.FileInputStream;
import java.sql.PreparedStatement;

import crypto.users.User;

public class dbmanagement
{
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    
    static{loadConfig();}
    
    private static void loadConfig() 
    {
        Properties props = new Properties();
        try 
        {
            FileInputStream input = null;
            try {input = new FileInputStream("config.properties");} 
            catch(IOException e) 
            {
                try{input = new FileInputStream("crypto/config.properties");} 
                catch(IOException e2) 
                {
                    System.err.println("Fichier de configuration non trouve. Utilisation des valeurs par defaut.");
                    return;
                }
            }
            
            props.load(input);
            input.close();
            
            DB_URL      = props.getProperty("db.url").trim();
            DB_USER     = props.getProperty("db.username").trim();
            DB_PASSWORD = props.getProperty("db.password").trim();
            
            System.out.println("Configuration chargee depuis config.properties");
            
        } 
        catch(IOException e){System.err.println("Erreur lors du chargement de config.properties: " + e.getMessage());}
    }
    
    public static Connection connect() 
    {
        try{return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);}
        
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to data base.", e);
        }
    }

    public static void create_db()
    {
        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS crypto_db;")) 
        {
            pstmt.execute();
        } 
        catch(SQLException e) {e.printStackTrace();}
    }

    public static void createTable() 
    {
        String query = "CREATE TABLE IF NOT EXISTS users("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "login VARCHAR(50) NOT NULL, "
                + "email VARCHAR(100) NOT NULL, "
                + "password VARCHAR(100) NOT NULL"
                + ");";

        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)){pstmt.execute();} 
        catch(SQLException e){e.printStackTrace();}
    }

    public static boolean registerUser(User user) 
    {
        String query = "INSERT INTO users(login, email, password) VALUES(?, ?, ?)";
        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.executeUpdate();
            return true;
        } 
        catch(SQLException e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyUser(String email, String password) 
    {
        // CORRIGE : requete parametree — injection SQL eliminee
        String query = "SELECT id FROM users WHERE email = ? AND password = ?";
        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } 
        catch(SQLException e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean userExists(String login, String email) 
    {
        String query = "SELECT * FROM users WHERE login = ? OR email = ?";
        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.setString(1, login);
            pstmt.setString(2, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } 
        catch(SQLException e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    public static void deleteUser(String login) 
    {
        String query = "DELETE FROM users WHERE login = ?";
        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.setString(1, login);
            pstmt.executeUpdate();
        } 
        catch(SQLException e) {e.printStackTrace();}
    }

    public static boolean updatePassword(String email, String newPassword) 
    {
        String query = "UPDATE users SET password = ? WHERE email = ?";
        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } 
        catch(SQLException e) 
        {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean emailExists(String email) 
    {
        String query = "SELECT * FROM users WHERE email = ?";
        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } 
        catch(SQLException e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUserByEmail(String hashedEmail) 
    {
        String query = "SELECT login, email, password FROM users WHERE email = ?";
        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.setString(1, hashedEmail);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) 
            {
                String login = rs.getString("login");
                String email = rs.getString("email");
                String password = rs.getString("password");

                return new User(login, email, password);
            }
        } 
        catch(SQLException e){e.printStackTrace();}
        
        return null;
    }
}