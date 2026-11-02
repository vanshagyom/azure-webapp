package com.example.demo.service;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobItem;
import com.example.demo.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentBlobService {

    private final BlobContainerClient container;
    private final ObjectMapper mapper = new ObjectMapper();

    public StudentBlobService(
            @Value("${azure.storage.account-name}") String account,
            @Value("${azure.storage.container-name}") String containerName) {

        BlobServiceClient service = new BlobServiceClientBuilder()
                .endpoint("https://" + account + ".blob.core.windows.net")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

        this.container = service.getBlobContainerClient(containerName);
    }

    private String blobName(int id) {
        return "students/" + id + ".json";
    }

    public void save(Student student) throws Exception {
        byte[] data = mapper.writeValueAsBytes(student);
        container.getBlobClient(blobName(student.getId()))
                .upload(new ByteArrayInputStream(data), data.length, true);
    }

    public Optional<Student> get(int id) throws Exception {
        BlobClient blob = container.getBlobClient(blobName(id));
        if (!blob.exists()) return Optional.empty();
        byte[] data = blob.downloadContent().toBytes();
        return Optional.of(mapper.readValue(data, Student.class));
    }

    public List<Student> getAll() throws Exception {
        List<Student> result = new ArrayList<>();
        for (BlobItem item : container.listBlobsByHierarchy("students/")) {
            if (item.isPrefix() != null && item.isPrefix()) continue;
            byte[] data = container.getBlobClient(item.getName()).downloadContent().toBytes();
            result.add(mapper.readValue(data, Student.class));
        }
        return result;
    }

    public void delete(int id) {
        container.getBlobClient(blobName(id)).deleteIfExists();
    }
}