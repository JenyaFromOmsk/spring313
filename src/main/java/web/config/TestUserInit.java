package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import web.Models.Role;
import web.Models.User;
import web.Service.RoleService;
import web.Service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class TestUserInit {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public TestUserInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    @Bean
    public void addTestUser() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");

        Set<Role> rolesForAdmin = new HashSet<>();
        rolesForAdmin.add(roleAdmin);
        rolesForAdmin.add(roleUser);

        for (Role role: rolesForAdmin) {
            roleService.addRole(role);
        }
        User userAdmin = new User("admin","admin");
        userAdmin.setRole(rolesForAdmin);
        userService.addUser(userAdmin);

        User userUser = new User("user","user");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleService.getRoleByName("ROLE_USER"));
        userUser.setRole(userRoles);
        userService.addUser(userUser);
    }


}
