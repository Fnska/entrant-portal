package ru.ncedu.frolov.entrantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ncedu.frolov.entrantportal.domain.Course;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.applications WHERE c.id = ?1")
    Optional<Course> findOneWithApplicationsById(Long id);

    List<Course> findByFaculty_Id(Long facultyId);
}
