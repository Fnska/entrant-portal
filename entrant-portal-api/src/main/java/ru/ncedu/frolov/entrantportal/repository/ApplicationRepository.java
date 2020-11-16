package ru.ncedu.frolov.entrantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ncedu.frolov.entrantportal.domain.Application;
import ru.ncedu.frolov.entrantportal.domain.enums.Priority;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByUser_Id(Long id);
    List<Application> findAllByCourse_Id(Long id);
    List<Application> findAllByRatingGreaterThan(Double rating);
    List<Application> findAllByRatingGreaterThanAndPriority(Double rating, Priority priority);
}
