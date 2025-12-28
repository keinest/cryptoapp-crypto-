package crypto.users;

import java.util.*;

public class UserStorage
{
    private List<User> userList;

    public UserStorage()
    {
        userList = new ArrayList<>();
    }

    public void addUser(User user)
    {
        userList.add(user);
    }

    public List getUsersList()
    {
        return this.userList;
    }
}