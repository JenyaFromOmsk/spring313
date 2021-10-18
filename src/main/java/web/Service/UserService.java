package web.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.Models.User;

import java.util.List;

@Service
public interface UserService {

    void addUser(User user);

    void deleteUserById(long id);

    void editUser(User user);

    User getUserId(Long id);

    List<User> listUsers();

    @Transactional
    User getUserByLogin(String login);
}