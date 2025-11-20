package com.grh.config;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grh.model.Appointment;
import com.grh.model.AppointmentStatus;
import com.grh.model.Doctor;
import com.grh.model.Gender;
import com.grh.model.Patient;
import com.grh.model.WorkSlot;
import com.grh.repository.AppointmentRepository;
import com.grh.repository.DoctorRepository;
import com.grh.repository.PatientRepository;
/*
 * Class كيتمشى فـ startup
كيعمر قاعدة البيانات بــ:
شوية ديال المرضى
شوية ديال الأطباء (مع workSlots)
شوية ديال المواعيد للتجريب
 */
@Configuration
public class DataSeeder {
  private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

  @Bean
  CommandLineRunner seed(DoctorRepository doctorRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
    return args -> {
      if (doctorRepository.count() == 0) {
        Doctor d1 = new Doctor();
        d1.setFullName("Dr. Alice Martin");
        d1.setSpecialty("Cardiologie");
        d1.setPhone("+33123456789");
        d1.setEmail("alice.martin@hospital.fr");
        d1.setWorkSlots(List.of(
            new WorkSlot(1, LocalTime.of(9, 0), LocalTime.of(12, 0)),
            new WorkSlot(3, LocalTime.of(14, 0), LocalTime.of(18, 0))
        ));
        var now = Instant.now();
        d1.setCreatedAt(now);
        d1.setUpdatedAt(now);
        doctorRepository.save(d1);

        Doctor d2 = new Doctor();
        d2.setFullName("Dr. Bob Dupont");
        d2.setSpecialty("Neurologie");
        d2.setPhone("+33987654321");
        d2.setEmail("bob.dupont@hospital.fr");
        d2.setWorkSlots(List.of(
            new WorkSlot(2, LocalTime.of(10, 0), LocalTime.of(16, 0)),
            new WorkSlot(4, LocalTime.of(9, 30), LocalTime.of(12, 30))
        ));
        d2.setCreatedAt(now);
        d2.setUpdatedAt(now);
        doctorRepository.save(d2);

        log.info("Seeded doctors");
      }

      if (patientRepository.count() == 0) {
        Patient p1 = new Patient();
        p1.setFullName("John Doe");
        p1.setDateOfBirth(LocalDate.of(1990, 5, 15));
        p1.setGender(Gender.M);
        p1.setPhone("+33600000001");
        p1.setEmail("john.doe@example.com");
        p1.setAddress("10 Rue de Paris, Paris");
        var now = Instant.now();
        p1.setCreatedAt(now);
        p1.setUpdatedAt(now);
        patientRepository.save(p1);

        Patient p2 = new Patient();
        p2.setFullName("Jane Smith");
        p2.setDateOfBirth(LocalDate.of(1985, 8, 22));
        p2.setGender(Gender.F);
        p2.setPhone("+33600000002");
        p2.setEmail("jane.smith@example.com");
        p2.setAddress("20 Avenue Lyon, Lyon");
        p2.setCreatedAt(now);
        p2.setUpdatedAt(now);
        patientRepository.save(p2);

        log.info("Seeded patients");
      }

      if (appointmentRepository.count() == 0) {
        List<Doctor> doctors = doctorRepository.findAll();
        List<Patient> patients = patientRepository.findAll();
        if (!doctors.isEmpty() && !patients.isEmpty()) {
          Doctor d = doctors.get(0);
          Patient p = patients.get(0);

          Appointment a1 = new Appointment();
          a1.setDoctorId(d.getId());
          a1.setPatientId(p.getId());
          a1.setDate(LocalDate.now());
          a1.setTime(LocalTime.of(9, 30));
          a1.setDurationMinutes(30);
          a1.setStatus(AppointmentStatus.Scheduled);
          var now = Instant.now();
          a1.setCreatedAt(now);
          a1.setUpdatedAt(now);
          appointmentRepository.save(a1);

          log.info("Seeded appointments");
        }
      }
    };
  }
}