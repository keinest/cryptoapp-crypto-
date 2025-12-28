package crypto.users.db;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

import crypto.users.User;

public class dbmanagement
{
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    
    static {
        loadConfig();
    }
    
    private static void loadConfig() 
    {
        Properties props = new Properties();
        try 
        {
            FileInputStream input = null;
            
            try {input = new FileInputStream("config.properties");} 
            catch (IOException e) 
            {
                try{input = new FileInputStream("crypto/config.properties");} 
                catch (IOException e2) 
                {
                    System.err.println("⚠️  Fichier config.properties non trouve. Utilisation des valeurs par defaut.");
                    setDefaultConfig();
                    return;
                }
            }
            
            props.load(input);
            input.close();
            
            DB_URL      = props.getProperty("db.url", "jdbc:mysql://localhost:3306/crypto_app");
            DB_USER     = props.getProperty("db.username", "crypto_user");
            DB_PASSWORD = props.getProperty("db.password", "db_User123");
            
            System.out.println("✅ Configuration chargee depuis config.properties");
            
        } 
        catch (IOException e) 
        {
            System.err.println("❌ Erreur lors du chargement de config.properties: " + e.getMessage());
            setDefaultConfig();
        }
    }
    
    private static void setDefaultConfig() 
    {
        DB_URL      = "jdbc:mysql://localhost:3306/crypto_app";
        DB_USER     = "crypto_user";
        DB_PASSWORD = "db_User123";
        System.out.println("Utilisation des valeurs par defaut:");
        System.out.println("  URL: " + DB_URL);
        System.out.println("  User: " + DB_USER);
    }

    public static Connection connect() 
    {
        try
        {
           return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
        
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to data base.", e);
        }
    }

    public static void createTable() 
    {
        String query = "CREATE TABLE IF NOT EXISTS users("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "login VARCHAR(50) NOT NULL, "
                + "email VARCHAR(100) NOT NULL, "
                + "password VARCHAR(100) NOT NULL"
                + ");";

        try(Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.execute();
        } 
        catch(SQLException e) 
        {
            e.printStackTrace();
        }
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
        String query = "SELECT * FROM users WHERE email = '" + email + "' AND password = '" + password + "'";
        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
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

    public static void deletUser(String login) 
    {
        String query = "DELETE FROM users WHERE login = ?";
        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.setString(1, login);
            pstmt.executeUpdate();
        } 
        catch(SQLException e) 
        {
            e.printStackTrace();
        }
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
