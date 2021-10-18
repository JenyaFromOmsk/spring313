package web.Dao;

import web.Models.Role;
import java.util.List;

public interface RoleDao {

    void addRole(Role role);

    void editRole(Role role);

    void deleteRoleById(long id);

    List<Role> getAllRoles();

    Role getRoleByName(String role);
}