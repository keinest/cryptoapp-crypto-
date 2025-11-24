package crypto.users;

public class User
{
    private String login;
    private String email;
    private String password;

    public User(String login,String email,String password)
    {
        this.login    = login;
        this.email    = email;
        this.password = password;
    }

    public String getLogin()
    {
        return this.login;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getPassword()
    {
        return this.password;
    }
}