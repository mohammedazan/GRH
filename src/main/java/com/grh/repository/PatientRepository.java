package com.grh.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.grh.model.Patient;
//→ تُستعمل من طرف الـ Services باش تجيب / تكتب البيانات.
public interface PatientRepository extends MongoRepository<Patient, String> {
  List<Patient> findByFullNameContainingIgnoreCase(String fullName);
  //findByFullNameContainingIgnoreCase(...) → بحث فـ الإسم
}