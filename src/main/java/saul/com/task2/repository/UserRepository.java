package saul.com.task2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import saul.com.task2.entity.Person;

public interface UserRepository extends JpaRepository<Person, Long> {
}
