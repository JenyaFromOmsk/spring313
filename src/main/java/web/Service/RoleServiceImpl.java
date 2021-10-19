package web.Service;

import org.springframework.beans.factory.annotation.Autowired;
import web.Models.Role;
import web.Dao.RoleDao;
import java.util.List;


public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao;

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public void addRole(Role role) {
        roleDao.addRole(role);
    }

    @Override
    public void updateRole(Role role) {
        roleDao.editRole(role);
    }

    @Override
    public void deleteRoleById(long id) {
        roleDao.deleteRoleById(id);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Override
    public Role getRoleByName(String role) {
        return roleDao.getRoleByName(role);
    }

}