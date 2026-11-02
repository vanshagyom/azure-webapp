package com.example.demo.controller;

import com.example.demo.model.Student;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    // In-memory array list
    private List<Student> students = new ArrayList<>();

    // Save student
    @PostMapping
    public String addStudent(@RequestBody Student student) {
        students.add(student);
        return "Student saved successfully!";
    }

    // Get all students
    @GetMapping
    public List<Student> getStudents() {
        return students;
    }

    // Get student by ID
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable int id) {
        return students.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Delete student
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable int id) {
        students.removeIf(s -> s.getId() == id);
        return "Student deleted!";
    }
}