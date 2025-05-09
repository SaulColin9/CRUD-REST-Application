package saul.com.task2.service;

import org.springframework.stereotype.Service;
import saul.com.task2.entity.UserEntity;
import saul.com.task2.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getUsers(){
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Long id){
        return userRepository.findById(id);
    }

    public UserEntity createUser(UserEntity user){
        return userRepository.save(user);
    }

    public UserEntity updateUser(Long id, UserEntity updatedUser){
        UserEntity foundUser = userRepository.findById(id).orElseThrow(()->new RuntimeException("User Not Found!"));
        foundUser.setName(updatedUser.getName());
        foundUser.setLastName(updatedUser.getLastName());
        return userRepository.save(foundUser);
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }
}
