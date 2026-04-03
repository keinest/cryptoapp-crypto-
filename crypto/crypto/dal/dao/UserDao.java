package crypto.dal.dao;

import crypto.dal.DbConnection;
import crypto.users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO (Data Access Object) pour les operations utilisateurs.
 *
 * SeCURITE : toutes les requetes utilisent des PreparedStatement parametres
 * afin d'eliminer les risques d'injection SQL.
 */

public class UserDao
{
    // -- Initialisation -------------

    /** Cree la table users si elle n'existe pas. */

    public static void ensureTableExists()
    {
        String sql =
            "CREATE TABLE IF NOT EXISTS users (" +
            "  id       INT AUTO_INCREMENT PRIMARY KEY, " +
            "  login    VARCHAR(50)  NOT NULL, " +
            "  email    VARCHAR(100) NOT NULL UNIQUE, " +
            "  password VARCHAR(100) NOT NULL" +
            ")";

        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.execute();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    // -- Lecture --------------------

    /**
     * Verifie les identifiants (email + mot de passe hashes).
    */

    public static boolean verifyUser(String hashedEmail, String hashedPassword)
    {
        String sql = "SELECT id FROM users WHERE email = ? AND password = ?";

        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1, hashedEmail);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /** Recupere un utilisateur par son email hashe. */
    public static User getUserByEmail(String hashedEmail)
    {
        String sql = "SELECT login, email, password FROM users WHERE email = ?";

        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1, hashedEmail);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return new User(rs.getString("login"), rs.getString("email"), rs.getString("password"));
        }
        catch (SQLException e) { e.printStackTrace(); }

        return null;
    }

    /** Verifie si un login ou email existe deja. */

    public static boolean userExists(String login, String email)
    {
        String sql = "SELECT id FROM users WHERE login = ? OR email = ?";

        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1, login);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /** Verifie si un email existe. */

    public static boolean emailExists(String hashedEmail)
    {
        String sql = "SELECT id FROM users WHERE email = ?";

        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1, hashedEmail);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // -- ecriture -------------------

    /** Enregistre un nouvel utilisateur. */

    public static boolean registerUser(User user)
    {
        String sql = "INSERT INTO users (login, email, password) VALUES (?, ?, ?)";

        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /** Met a jour le mot de passe d'un utilisateur. */

    public static boolean updatePassword(String hashedEmail, String newHashedPassword)
    {
        String sql = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1, newHashedPassword);
            ps.setString(2, hashedEmail);
            return ps.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /** Supprime un utilisateur par son login. */

    public static boolean deleteUser(String login)
    {
        String sql = "DELETE FROM users WHERE login = ?";

        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1, login);
            return ps.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
