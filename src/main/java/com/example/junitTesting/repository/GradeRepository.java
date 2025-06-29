package com.example.junitTesting.repository;

import com.example.junitTesting.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GradeRepository extends JpaRepository<Grade,Long> {
    List<Grade> findByStudentId(Long studentId);
//    List<Grade> findBySubject(String subject);
}
