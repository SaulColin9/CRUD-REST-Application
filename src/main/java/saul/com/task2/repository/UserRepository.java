package saul.com.task2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import saul.com.task2.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
