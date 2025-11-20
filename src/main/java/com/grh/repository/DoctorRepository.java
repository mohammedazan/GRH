package com.grh.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.grh.model.Doctor;
//→ تُستعمل من طرف الـ Services باش تجيب / تكتب البيانات.
public interface DoctorRepository extends MongoRepository<Doctor, String> {
  List<Doctor> findByFullNameContainingIgnoreCase(String fullName);
  List<Doctor> findBySpecialtyContainingIgnoreCase(String specialty);
}