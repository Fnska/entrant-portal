package ru.ncedu.frolov.entrantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ncedu.frolov.entrantportal.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.educations WHERE u.id = ?1")
    Optional<User> findOneWithEducationsById(Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.applications WHERE u.id = ?1")
    Optional<User> findOneWithApplicationsById(Long id);
}
