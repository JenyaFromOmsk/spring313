package web.Service;

import web.Models.Role;

import java.util.List;

public interface RoleService {

    void addRole(Role role);

    void updateRole(Role role);

    void deleteRoleId(long id);

    List<Role> getAllRoles();

    Role getRoleName(String name);
}
