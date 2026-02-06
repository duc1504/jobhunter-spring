package vn.developer.jobhunter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.developer.jobhunter.domain.User;
import vn.developer.jobhunter.service.UserService;

@RestController
public class UserController {
    private final UserService userService;
    
     public UserController(UserService userService) {
        this.userService = userService;
    }
    // create new user
     @PostMapping("/user")
    public User creatNewUser(@RequestBody User userinput) {
        User user = new User();
        user.setName(userinput.getName());
        user.setEmail(userinput.getEmail());
        user.setPassword(userinput.getPassword());
      User newUser =  userService.handleCreateUser(user);
        return newUser;
    }

    // delete user by id
    @DeleteMapping("/user/{id}")
    public String deleteUserById(@PathVariable long id) {
    userService.handleDeleteUser(id);
        return "dêlete user by id: " + id;
    }

    // get user by id
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.handleGetUserById(id);
    }

    // get all user
    @GetMapping("/user")
    public List<User> getAllUser() {
        return userService.handleGetAllUser();
    }

    // update user
    @PutMapping("/user")
    public User updateUser(@RequestBody User userinput) {
        return this.userService.handleUpdateUser(userinput);
    }
}

