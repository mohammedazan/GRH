package com.grh.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.grh.model.Doctor;

public interface DoctorRepository extends MongoRepository<Doctor, String> {
  List<Doctor> findByFullNameContainingIgnoreCase(String fullName);
  List<Doctor> findBySpecialtyContainingIgnoreCase(String specialty);
}