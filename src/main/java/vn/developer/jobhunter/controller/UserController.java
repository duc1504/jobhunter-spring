package vn.developer.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     @PostMapping("/users")
    public ResponseEntity<User> creatNewUser(@RequestBody User userinput) {
        User user = new User();
        user.setName(userinput.getName());
        user.setEmail(userinput.getEmail());
        user.setPassword(userinput.getPassword());
      User newUser =  userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // delete user by id
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable long id) {
    userService.handleDeleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // get user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.handleGetUserById(id));
    }

    // get all user
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {
         return ResponseEntity.status(HttpStatus.OK).body(userService.handleGetAllUser());
    }

    // update user
    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User userinput) {
       return    ResponseEntity.status(HttpStatus.OK).body(this.userService.handleUpdateUser(userinput));
    }
}

