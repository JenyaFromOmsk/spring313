package web.Dao;

import web.Models.Role;
import java.util.List;

public interface RoleDao {

    void addRole(Role role);

    void editRole(Role role);

    void deleteRoleId(long id);

    List<Role> getAllRoles();

    Role getRoleName(String role);
}