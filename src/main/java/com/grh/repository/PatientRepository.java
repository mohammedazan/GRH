package com.grh.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.grh.model.Patient;

public interface PatientRepository extends MongoRepository<Patient, String> {
  List<Patient> findByFullNameContainingIgnoreCase(String fullName);
}