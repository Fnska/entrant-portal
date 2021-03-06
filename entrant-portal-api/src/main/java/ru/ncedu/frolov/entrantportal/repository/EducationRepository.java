package ru.ncedu.frolov.entrantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ncedu.frolov.entrantportal.domain.Education;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    @Query("SELECT e FROM Education e LEFT JOIN FETCH e.user WHERE e.user.id = ?1")
    List<Education> findByUserId(Long userId);
}
