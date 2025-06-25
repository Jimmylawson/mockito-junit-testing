package com.example.junitTesting.integration;


import com.example.junitTesting.entity.Student;
import com.example.junitTesting.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.Random.class)
public class StudentIntegrationTest {
    @Autowired
    private  StudentRepository studentRepository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        studentRepository.save(Student.builder()
                .name("Aayush")
                .email("ayush@gmail.com")
                .build());
    }

    @Test
    void testSaveStudent(){
        var student = studentRepository.save(Student.builder()
                .name("New")
                .email("new@gmail.com")
                .build());

        var found = studentRepository.findById(student.getId());
        assertTrue(found.isPresent());
        assertEquals("New",found.get().getName());
    }

    @Test
    void testAllStudents(){

        /// Act-Retrieve all students
        var students = studentRepository.findAll();


        /// Assert -Check that there's exactly one student
        assertEquals(1,students.size());

    }

    @Test

    void testStudentById() throws Exception {
        var saved = studentRepository.save(Student.builder()
                .name("John Doe")
                .email("john@gmail.com")
                .build());

        mockMvc.perform(get("/students/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        assertTrue(studentRepository.existsById(saved.getId()));
    }


    @Test
    void testDeleteStudentById() throws Exception{
        var deleteStudent = studentRepository.save(Student.builder()
                .name("ToDeleted")
                .email("deleted@gmail.com")
                .build());

        mockMvc.perform(delete("/students/{id}", deleteStudent.getId()))
                .andExpect(status().isNoContent());

        assertFalse(studentRepository.existsById(deleteStudent.getId()));

    }
}
