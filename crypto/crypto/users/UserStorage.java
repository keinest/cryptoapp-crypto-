package crypto.users;

import java.util.List;
import java.util.ArrayList;

public class UserStorage
{
    private List<User> userList;

    public UserStorage()
    {
        this.userList = new ArrayList<>();
    }

    public void addUser(User user)
    {
        this.userList.add(user);
    }

    public List getUsersList()
    {
        return this.userList;
    }
}