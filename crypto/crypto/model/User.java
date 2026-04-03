package crypto.model;

/**
 * Modèle de données utilisateur (immuable après construction).
 */
public class User
{
    private final String login;
    private final String email;
    private final String password;

    public User(String login, String email, String password)
    {
        this.login    = login;
        this.email    = email;
        this.password = password;
    }

    public String getLogin()    { return login;    }
    public String getEmail()    { return email;    }
    public String getPassword() { return password; }

    @Override
    public String toString()
    {
        return "User{login='" + login + "', email='" + email + "'}";
    }
}
