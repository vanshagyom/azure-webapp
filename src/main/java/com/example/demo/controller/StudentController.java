package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.service.StudentBlobService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentBlobService service;

    public StudentController(StudentBlobService service) {
        this.service = service;
    }

    @PostMapping
    public String addStudent(@RequestBody Student student) throws Exception {
        service.save(student);
        return "Student saved successfully!";
    }

    @GetMapping
    public List<Student> getStudents() throws Exception {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable int id) throws Exception {
        return service.get(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable int id) {
        service.delete(id);
        return "Student deleted!";
    }
}