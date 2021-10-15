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
        User admin = new User();
        User user = new User();

        userService.addUser(admin);
        userService.addUser(user);

        Role role1 = new Role();
        Role role2 = new Role();

        roleService.addRole(role1);
        roleService.addRole(role2);

        role1.setRole("ROLE_ADMIN");
        role2.setRole("ROLE_USER");

        roleService.updateRole(role1);
        roleService.updateRole(role2);

        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);

        admin.setLogin("admin");
        admin.setPassword("admin");
        admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        admin.setRole(roles);
        userService.editUser(admin);

        user.setLogin("user");
        user.setPassword("user");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(roles.stream().skip(1).collect(Collectors.toSet()));
        userService.editUser(user);
    }

}