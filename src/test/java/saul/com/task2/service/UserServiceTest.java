package saul.com.task2.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import saul.com.task2.entity.UserEntity;
import saul.com.task2.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public UserServiceTest(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser(){
        UserEntity user = new UserEntity("Saul", "Colin");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity createdUser = userService.createUser(user);

        assertThat(createdUser.getName()).isEqualTo("Saul");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserById(){
        UserEntity user = new UserEntity("Saul", "Colin");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Optional<UserEntity> foundUser = userService.getUserById(1L);
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Saul");
        assertThat(foundUser.get().getLastName()).isEqualTo("Colin");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateUser(){
        UserEntity existingUser = new UserEntity("Saul", "Colin");
        existingUser.setId(1L);

        UserEntity updatedUser = new UserEntity("Alejandro", "Salas");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(existingUser);

        UserEntity newUpdatedUser = userService.updateUser(1L, updatedUser);

        assertThat(newUpdatedUser.getName()).isEqualTo("Alejandro");
        assertThat(newUpdatedUser.getLastName()).isEqualTo("Salas");
    }

}