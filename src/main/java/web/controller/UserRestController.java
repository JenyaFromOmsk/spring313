package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.Exception.NoSuchUserException;
import web.Models.Role;
import web.Models.User;
import web.Service.RoleService;
import web.Service.UserService;
import web.Exception.IncorrectUser;

import java.util.List;

@RestController
@RequestMapping("/")
public class UserRestController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin/allUsers")
    public List<User> showAllUsers() {
        return userService.listUsers();
    }

    @GetMapping("/admin/{id}")
    public User getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            throw new NoSuchUserException("There is no user with ID: " + id + " in Database");
        }
        return user;
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectUser> handleException(Exception exception) {
        IncorrectUser incorrectUser = new IncorrectUser();
        incorrectUser.setInfo(exception.getMessage());

        return new ResponseEntity<>(incorrectUser, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/admin")
    public User addUser(@RequestBody User user) {
        userService.addUser(user);

        return user;
    }

    @GetMapping("/admin/allRoles")
    public List<Role> showAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/admin")
    public User editUser (@RequestBody User user) {
        userService.editUser(user);

        return user;
    }

    @DeleteMapping("/admin/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUserById(id);
    }


}