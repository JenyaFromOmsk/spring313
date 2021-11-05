package web.Service;

import org.springframework.stereotype.Service;
import web.Models.User;
import java.util.List;

@Service
public interface UserService {

    void addUser(User user);

    void deleteUserById(long id);

    void editUser(User user);

    User getUserById(Long id);

    List<User> listUsers();

    User getUserByLogin(String login);

    User getUserByName(String name);

    boolean existsUserById(long id);
}