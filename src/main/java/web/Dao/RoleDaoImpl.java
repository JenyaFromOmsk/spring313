package web.Dao;

import org.springframework.stereotype.Repository;
import web.Models.Role;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public void editRole(Role role) {
        entityManager.merge(role);
    }

    @Override
    public void deleteRoleById(long id) {
        entityManager.remove(entityManager.find(Role.class, id));
    }

    @Override
    public List<Role> getAllRoles() {
        return entityManager.createQuery("FROM Role").getResultList();
    }

    @Override
    public Role getRoleByName(String role) {
        return entityManager.createQuery("SELECT role FROM Role role WHERE role.role=:role",
                Role.class).setParameter("role", role).getSingleResult();
    }
}