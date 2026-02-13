package vn.developer.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import vn.developer.jobhunter.domain.User;
import vn.developer.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //  handle create user
    public User handleCreateUser(User user) {
      return this.userRepository.save(user);
    }
    //  handle delete user
    public void handleDeleteUser(long id) {
    this.userRepository.deleteById(id);
    }

    // handle get user by id
    public User handleGetUserById (long id){
       return  this.userRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id=" + id, null));
      
    }

    // handle get all user
    public List<User> handleGetAllUser () {
        return this.userRepository.findAll();
    }
    // hanlde update user
    public User handleUpdateUser(User user) {
        User userUpdate = this.handleGetUserById(user.getId()); 
        userUpdate.setName(user.getName());
        userUpdate.setEmail(user.getEmail());
        userUpdate.setPassword(user.getPassword());
        return this.userRepository.save(userUpdate);
    }
    // handle get user by email
    public User handleGetUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email=" + email, null));
    }
}
