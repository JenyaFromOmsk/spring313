package web.Service;

import org.springframework.stereotype.Service;
import web.Models.Role;

import java.util.List;

@Service
public interface RoleService {

    void addRole(Role role);

    void updateRole(Role role);

    void deleteRoleById(long id);

    List<Role> getAllRoles();

    Role getRoleByName(String name);
}
