package web.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.Models.Role;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {

    Role findByRole(String role);
}