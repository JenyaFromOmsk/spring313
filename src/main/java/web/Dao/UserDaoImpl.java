package web.Dao;

import org.springframework.stereotype.Repository;
import web.Models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void deleteUserId(long id) {
        entityManager.createQuery("DELETE FROM User user WHERE user.id=: id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void editUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public User getUserId(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("FROM User").getResultList();
    }

    @Override
    public User getLogin(String login) {
        TypedQuery<User> typedQuery = entityManager.createQuery("SELECT user FROM User user WHERE user.login=:login",
                User.class).setParameter("login", login);
    return typedQuery.getSingleResult();
    }
}