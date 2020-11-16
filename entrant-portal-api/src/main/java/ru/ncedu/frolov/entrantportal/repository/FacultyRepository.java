package ru.ncedu.frolov.entrantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ncedu.frolov.entrantportal.domain.Faculty;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}
