package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.Models.Role;
import web.Models.User;
import web.Service.RoleService;
import web.Service.UserService;
import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
public class UserController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String startPage() {
        return "redirect:/login";
    }

    @GetMapping(value = "/user")
    public String userInfo(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", user.getRole());
        return "user";
    }

    @GetMapping(value = "/admin")
    public String listUsers(Model model) {
        model.addAttribute("allUsers", userService.listUsers());
        return "allUsers";
    }

    @GetMapping(value = "/admin/add")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "addUser";
    }


    @PostMapping(value = "/admin/add-user")
    public String addUser(@ModelAttribute User user, @RequestParam(value = "choiceOfRole") String[] choiceOfRole) {
        Set<Role> set = new HashSet<>();
        for (String role : choiceOfRole) {
            set.add(roleService.getRoleName(role));
        }
        user.setRole(set);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/edit/{id}")
    public String editUserForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUserId(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "editUser";
    }

    @PostMapping(value = "/edit")
    public String editUser(@ModelAttribute User user, @RequestParam(value = "checkBoxRoles") String[] checkBoxRoles) {
        Set<Role> set = new HashSet<>();
        for (String roles : checkBoxRoles) {
            set.add(roleService.getRoleName(roles));
        }
        user.setRole(set);
        userService.editUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/remove/{id}")
    public String removeUser(@PathVariable("id") long id) {
        userService.deleteUserId(id);
        return "redirect:/admin";
    }

    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct 
    public void addTestUsers() {
        
        User userAdmin = new User();
        userService.addUser(userAdmin);
        User userUser = new User();
        userService.addUser(userUser);
        
        Role roleAdmin = new Role();
        roleService.addRole(roleAdmin);
        roleAdmin.setRole("ROLE_ADMIN");
        roleService.updateRole(roleAdmin);
        
        Role roleUser = new Role();
        roleService.addRole(roleUser);
        roleUser.setRole("ROLE_USER");
        roleService.updateRole(roleUser);
        
        Set<Role> setRoleAdmin = new HashSet<>();
        setRoleAdmin.add(roleAdmin);
        setRoleAdmin.add(roleUser);

        Set<Role> setRoleUser = new HashSet<>();
        setRoleUser.add(roleUser);

        userAdmin.setLogin("admin");
        userAdmin.setPassword("admin");
        userAdmin.setPassword(bCryptPasswordEncoder.encode(userAdmin.getPassword()));
        userAdmin.setRole(setRoleAdmin);
        userService.editUser(userAdmin);

        userUser.setLogin("user");
        userUser.setPassword("user");
        userUser.setPassword(bCryptPasswordEncoder.encode(userUser.getPassword()));
        userUser.setRole(setRoleUser);
        userService.editUser(userUser);
    }

}
