package saul.com.task2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import saul.com.task2.entity.UserEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser(){
        UserEntity user = new UserEntity("Saul", "Colin");
        UserEntity savedUser = userRepository.save(user);

        assertThat(savedUser.getName()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Saul");
        assertThat(savedUser.getLastName()).isEqualTo("Colin");
    }

    @Test
    void testFindUserById(){
        UserEntity user = new UserEntity("Saul", "Colin");
        UserEntity savedUser = userRepository.save(user);

        Optional<UserEntity> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Saul");
        assertThat(foundUser.get().getLastName()).isEqualTo("Colin");
    }

    @Test
    void testDeleteUser(){
        UserEntity user = new UserEntity("Saul", "Colin");
        UserEntity savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());
        Optional<UserEntity> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isNotPresent();
    }

}