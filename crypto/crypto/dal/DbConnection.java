package crypto.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Gestion centralisee de la connexion a la base de donnees.
 */
public class DbConnection
{
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    static { loadConfig(); }

    private static void loadConfig()
    {
        Properties props = new Properties();
        String[] paths   = {"config.properties", "crypto/config.properties"};

        for (String path : paths)
        {
            try (FileInputStream in = new FileInputStream(path))
            {
                props.load(in);
                DB_URL      = props.getProperty("db.url",      "").trim();
                DB_USER     = props.getProperty("db.username", "").trim();
                DB_PASSWORD = props.getProperty("db.password", "").trim();
                return;
            }
            catch (IOException ignored) {}
        }

        System.err.println("[DbConnection] config.properties introuvable — BD desactivee.");
    }

    /**
     * Ouvre et retourne une nouvelle connexion.
     * @throws RuntimeException si la connexion echoue.
     */
    
    public static Connection getConnection()
    {
        if (DB_URL == null || DB_URL.isEmpty())
            throw new RuntimeException("URL de base de donnees non configuree.");

        try
        {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Impossible de se connecter a la base de donnees.", e);
        }
    }

    /** Verifie si la configuration BD est disponible. */
    public static boolean isConfigured()
    {
        return DB_URL != null && !DB_URL.isEmpty();
    }
}
