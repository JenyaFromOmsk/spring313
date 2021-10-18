package web.Dao;

import web.Models.User;

import java.util.List;

public interface UserDao {

    void addUser(User user);

    void editUser(User user);

    void deleteUserId (long id);

    User getUserById(Long id);

    List<User> getAllUsers();

    User getUserByLogin(String login);
}