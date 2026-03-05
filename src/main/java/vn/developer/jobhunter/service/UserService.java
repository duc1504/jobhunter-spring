package vn.developer.jobhunter.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import vn.developer.jobhunter.domain.Company;
import vn.developer.jobhunter.domain.User;
import vn.developer.jobhunter.domain.dto.searchDTO.UserSearchDTO;
import vn.developer.jobhunter.domain.response.ResCreateUserDTO;
import vn.developer.jobhunter.domain.response.ResUserDTO;
import vn.developer.jobhunter.domain.response.RestUpdateUserDTO;
import vn.developer.jobhunter.domain.response.ResultPaginationDTO;
import vn.developer.jobhunter.domain.specification.UserSpecification;
import vn.developer.jobhunter.repository.UserRepository;
import vn.developer.jobhunter.util.error.ResourceNotFoundException;

@Service
public class UserService {

    private final CompanyService companyService;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    //  handle create user
    public User handleCreateUser(User user) {
        // check company 
        if (user.getCompany() !=null) {
            Optional<Company> companyOptional = this.companyService.handleGetCompanyById(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null );
        }
      return this.userRepository.save(user);
    }
    //  handle delete user
    public void handleDeleteUser(long id) {
    this.userRepository.deleteById(id);
    }

    // handle get user by id
    public User handleGetUserById (long id){
       return  this.userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id=" + id));
      
    }

    // handle get all user
    public ResultPaginationDTO<ResUserDTO> handleGetAllUser (UserSearchDTO filter,Pageable pageable){ 
        Specification<User> spec = UserSpecification.buildFilterUser(filter);
        Page<User> page = this.userRepository.findAll(spec,pageable);
        Page<ResUserDTO> UserDTO = page.map(user -> 
            new ResUserDTO(user.getId(),
            user.getName(),
            user.getEmail(),
            user.getGender(),
            user.getAddress(),
            user.getAge(),
            user.getCreatedAt(),
            user.getUpdatedAt(), 
            new ResUserDTO.Company(
                user.getCompany() != null ? user.getCompany().getId() : 0,
                user.getCompany() != null ? user.getCompany().getName() : null)));
        ResultPaginationDTO<ResUserDTO> result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        result.setMeta(meta);
        result.setResult(UserDTO.getContent());
        return result;
    }
    // hanlde update user
    public User handleUpdateUser(User user) {
         User userUpdate = this.handleGetUserById(user.getId()); 
        if (user.getCompany() !=null) {
            Optional<Company> companyOptional = this.companyService.handleGetCompanyById(user.getCompany().getId());
            userUpdate.setCompany(companyOptional.isPresent() ? companyOptional.get() : null );
        }
       
        userUpdate.setName(user.getName());
        userUpdate.setAddress(user.getAddress());
        userUpdate.setAge(user.getAge());
        userUpdate.setGender(user.getGender());
        return this.userRepository.save(userUpdate);
    }
    // handle get user by email
    public User handleGetUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email=" + email, null));
    }
    // exits by email
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    // convert User to ResCreateUserDTO
    public ResCreateUserDTO convertUserToResCreateUserDTO(User user) {
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();
        resCreateUserDTO.setId(user.getId());
        resCreateUserDTO.setName(user.getName());
        resCreateUserDTO.setEmail(user.getEmail());
        resCreateUserDTO.setGender(user.getGender().name());
        resCreateUserDTO.setAddress(user.getAddress());
        resCreateUserDTO.setAge(user.getAge());
        resCreateUserDTO.setCreatedAt(user.getCreatedAt());
        if (user.getCompany() != null ) {
            ResCreateUserDTO.Company company = new ResCreateUserDTO.Company();
            company.setId(user.getCompany().getId());
            company.setName(user.getCompany().getName());
            resCreateUserDTO.setCompany(company);
        }
        return resCreateUserDTO;
    }
    //convert User to ResUserDTO
    public ResUserDTO convertUserToResUserDTO(User user) {
        ResUserDTO resUserDTO = new ResUserDTO();
        resUserDTO.setId(user.getId());
        resUserDTO.setName(user.getName());
        resUserDTO.setEmail(user.getEmail());
        resUserDTO.setGender(user.getGender());
        resUserDTO.setAddress(user.getAddress());
        resUserDTO.setAge(user.getAge());
        resUserDTO.setCreatedAt(user.getCreatedAt());
        resUserDTO.setUpdatedAt(user.getUpdatedAt());
        if (user.getCompany() != null) {
            ResUserDTO.Company company = new ResUserDTO.Company();
            company.setId(user.getCompany().getId());
            company.setName(user.getCompany().getName());
            resUserDTO.setCompany(company);
        }
        return resUserDTO;
    }
    // convert User to RestUpdateUserDTO
    public RestUpdateUserDTO convertUserToResUpdateUserDTO(User user) {
        RestUpdateUserDTO restUpdateUserDTO = new RestUpdateUserDTO();
        restUpdateUserDTO.setId(user.getId());
        restUpdateUserDTO.setName(user.getName());
        restUpdateUserDTO.setGender(user.getGender().name());
        restUpdateUserDTO.setAddress(user.getAddress());
        restUpdateUserDTO.setAge(user.getAge());
        restUpdateUserDTO.setUpdatedAt(user.getUpdatedAt());
        if (user.getCompany() != null) {
            RestUpdateUserDTO.Company company = new RestUpdateUserDTO.Company();
            company.setId(user.getCompany().getId());
            company.setName(user.getCompany().getName());
            restUpdateUserDTO.setCompany(company);
        }
        return restUpdateUserDTO;
    }

    // update user token
    public void updateUserToken(String email, String refreshToken) {
        User user = this.handleGetUserByEmail(email);
        user.setRefreshToken(refreshToken);
        this.userRepository.save(user);
    }

    public User handleGetUserByRefreshToken(String refreshToken,String email) {
        return this.userRepository.findByRefreshTokenAndEmail(refreshToken,email);
    }
}
