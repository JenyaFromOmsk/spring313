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
    public String addUser(@ModelAttribute User user, @RequestParam(value = "checkBoxRoles") String[] checkBoxRoles) {
        Set<Role> set = new HashSet<>();
        for (String role : checkBoxRoles) {
            set.add(roleService.getRoleByName(role));
        }
        user.setRole(set);
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/edit/{id}")
    public String editUserForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "editUser";
    }

    @PostMapping(value = "/edit")
    public String editUser(@ModelAttribute User user, @RequestParam(value = "checkBoxRoles") String[] checkBoxRoles) {
        Set<Role> set = new HashSet<>();
        for (String roles : checkBoxRoles) {
            set.add(roleService.getRoleByName(roles));
        }
        user.setRole(set);
        userService.editUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/remove/{id}")
    public String removeUser(@PathVariable("id") long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

}
