package vn.developer.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.developer.jobhunter.domain.User;
import vn.developer.jobhunter.domain.dto.searchDTO.UserSearchDTO;
import vn.developer.jobhunter.domain.response.ResCreateUserDTO;
import vn.developer.jobhunter.domain.response.ResUserDTO;
import vn.developer.jobhunter.domain.response.RestUpdateUserDTO;
import vn.developer.jobhunter.domain.response.ResultPaginationDTO;
import vn.developer.jobhunter.service.UserService;
import vn.developer.jobhunter.util.annotation.ApiMessage;
import vn.developer.jobhunter.util.error.IdInvaliException;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
     
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

     // create new user
     @PostMapping("/users")
     @ApiMessage(value = "create a new user")
    public ResponseEntity<ResCreateUserDTO> creatNewUser(@Valid @RequestBody User userinput) throws IdInvaliException {
        boolean emailExists = userService.existsByEmail(userinput.getEmail());
        if (emailExists) {
            throw new IdInvaliException("Email " + userinput.getEmail() + " already exists");
        }
        User user = new User();
        user.setName(userinput.getName());
        user.setEmail(userinput.getEmail());
        user.setPassword(passwordEncoder.encode(userinput.getPassword()));
        user.setAge(userinput.getAge());
        user.setGender(userinput.getGender());
        user.setAddress(userinput.getAddress());
      User newUser =  userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.convertUserToResCreateUserDTO(newUser));
    }

    // delete user by id
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long id) throws IdInvaliException {
        User user = userService.handleGetUserById(id);
        if (user == null) {
            throw new IdInvaliException("User not found with id=" + id);
        }
    userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }

    // get user by id
    @GetMapping("/users/{id}")
    @ApiMessage(value = "fetch user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable long id) {
        User user = userService.handleGetUserById(id);
        if (user == null) {
            throw new IdInvaliException("User not found with id=" + id);
        }
        User userget = userService.handleGetUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.convertUserToResUserDTO(userget));
    }

    // get all user
    @GetMapping("/users")
    @ApiMessage(value = "fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
        UserSearchDTO filter,
        Pageable pageable
    ) {
        // Pageable pageable = PageRequest.of(currentPage-1, pageSize);
         return ResponseEntity.status(HttpStatus.OK).body(userService.handleGetAllUser(filter,pageable));
    }

    // update user
    @PutMapping("/users")
    public ResponseEntity<RestUpdateUserDTO> updateUser(@RequestBody User userinput){
        
        User user = this.userService.handleUpdateUser(userinput);
       return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertUserToResUpdateUserDTO(user));
    }

}

