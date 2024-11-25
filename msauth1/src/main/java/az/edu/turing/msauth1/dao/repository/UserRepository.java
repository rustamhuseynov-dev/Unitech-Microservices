package az.edu.turing.msauth1.dao.repository;

import az.edu.turing.msauth1.dao.entity.UserEntity;
import az.edu.turing.msauth1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
