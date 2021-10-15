package web.Service;

import org.springframework.transaction.annotation.Transactional;
import web.Models.User;

import java.util.List;

public interface UserService {

    void addUser(User user);

    void deleteUserId(long id);

    void editUser(User user);

    User getUserId(Long id);

    List<User> listUsers();

    @Transactional
    User getLogin(String login);
}