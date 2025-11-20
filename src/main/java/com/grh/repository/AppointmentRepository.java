package com.grh.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.grh.model.Appointment;
import com.grh.model.AppointmentStatus;
//→ تُستعمل من طرف الـ Services باش تجيب / تكتب البيانات.
public interface AppointmentRepository extends MongoRepository<Appointment, String> {
  List<Appointment> findByDoctorId(String doctorId);
  List<Appointment> findByPatientId(String patientId);
  List<Appointment> findByDate(LocalDate date);
  List<Appointment> findByDoctorIdAndDate(String doctorId, LocalDate date);
  List<Appointment> findByDoctorIdAndDateAndStatusIn(String doctorId, LocalDate date, Collection<AppointmentStatus> statuses);
}