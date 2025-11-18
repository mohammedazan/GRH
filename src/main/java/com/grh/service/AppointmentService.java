package com.grh.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.grh.dto.AppointmentCreateRequest;
import com.grh.dto.AppointmentResponse;
import com.grh.dto.AppointmentUpdateRequest;
import com.grh.exception.ConflictException;
import com.grh.exception.NotFoundException;
import com.grh.mapper.AppointmentMapper;
import com.grh.model.Appointment;
import com.grh.model.AppointmentStatus;
import com.grh.model.Doctor;
import com.grh.model.WorkSlot;
import com.grh.repository.AppointmentRepository;
import com.grh.repository.DoctorRepository;
import com.grh.repository.PatientRepository;

@Service
public class AppointmentService {
  private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);
  private final AppointmentRepository appointmentRepository;
  private final DoctorRepository doctorRepository;
  private final PatientRepository patientRepository;

  public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
    this.appointmentRepository = appointmentRepository;
    this.doctorRepository = doctorRepository;
    this.patientRepository = patientRepository;
  }

  public AppointmentResponse create(AppointmentCreateRequest request) {
    patientRepository.findById(request.getPatientId()).orElseThrow(() -> new NotFoundException("Patient not found"));
    Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElseThrow(() -> new NotFoundException("Doctor not found"));
    int duration = request.getDurationMinutes() != null ? request.getDurationMinutes() : 30;
    validateDoctorAvailability(doctor, request.getDate(), request.getTime(), duration);
    checkConflict(doctor.getId(), request.getDate(), request.getTime(), duration, null);
    Appointment a = new Appointment();
    a.setPatientId(request.getPatientId());
    a.setDoctorId(request.getDoctorId());
    a.setDate(request.getDate());
    a.setTime(request.getTime());
    a.setDurationMinutes(duration);
    a.setStatus(AppointmentStatus.Scheduled);
    Instant now = Instant.now();
    a.setCreatedAt(now);
    a.setUpdatedAt(now);
    a = appointmentRepository.save(a);
    log.info("Appointment created: {}", a.getId());
    return AppointmentMapper.toResponse(a);
  }

  public AppointmentResponse update(String id, AppointmentUpdateRequest request) {
    Appointment existing = appointmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Appointment not found"));
    Doctor doctor = doctorRepository.findById(existing.getDoctorId()).orElseThrow(() -> new NotFoundException("Doctor not found"));
    LocalDate date = request.getDate() != null ? request.getDate() : existing.getDate();
    LocalTime time = request.getTime() != null ? request.getTime() : existing.getTime();
    int duration = request.getDurationMinutes() != null ? request.getDurationMinutes() : existing.getDurationMinutes();
    validateDoctorAvailability(doctor, date, time, duration);
    checkConflict(doctor.getId(), date, time, duration, existing.getId());
    existing.setDate(date);
    existing.setTime(time);
    existing.setDurationMinutes(duration);
    existing.setUpdatedAt(Instant.now());
    existing = appointmentRepository.save(existing);
    log.info("Appointment updated: {}", existing.getId());
    return AppointmentMapper.toResponse(existing);
  }

  public void cancel(String id) {
    Appointment existing = appointmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Appointment not found"));
    existing.setStatus(AppointmentStatus.Cancelled);
    existing.setUpdatedAt(Instant.now());
    appointmentRepository.save(existing);
    log.info("Appointment cancelled: {}", id);
  }

  public void deleteHard(String id) {
    Appointment existing = appointmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Appointment not found"));
    appointmentRepository.delete(existing);
    log.info("Appointment deleted: {}", id);
  }

  public AppointmentResponse getById(String id) {
    autoTerminatePastAppointments();
    Appointment a = appointmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Appointment not found"));
    return AppointmentMapper.toResponse(a);
  }

  public List<AppointmentResponse> getAll(String type) {
    autoTerminatePastAppointments();
    List<Appointment> list = appointmentRepository.findAll();
    list = filterByType(list, type);
    list = list.stream().sorted(Comparator.comparing(Appointment::getDate).thenComparing(Appointment::getTime)).toList();
    return list.stream().map(AppointmentMapper::toResponse).toList();
  }

  public List<AppointmentResponse> getByDay(LocalDate date) {
    autoTerminatePastAppointments();
    List<Appointment> list = appointmentRepository.findByDate(date);
    list = list.stream().sorted(Comparator.comparing(Appointment::getTime)).toList();
    return list.stream().map(AppointmentMapper::toResponse).toList();
  }

  public List<AppointmentResponse> getByDoctor(String doctorId) {
    autoTerminatePastAppointments();
    List<Appointment> list = appointmentRepository.findByDoctorId(doctorId);
    return list.stream().map(AppointmentMapper::toResponse).toList();
  }

  public List<AppointmentResponse> getByPatient(String patientId) {
    autoTerminatePastAppointments();
    List<Appointment> list = appointmentRepository.findByPatientId(patientId);
    return list.stream().map(AppointmentMapper::toResponse).toList();
  }

  private void validateDoctorAvailability(Doctor doctor, LocalDate date, LocalTime time, int duration) {
    int weekday = date.getDayOfWeek().getValue();
    List<WorkSlot> slots = doctor.getWorkSlots() == null ? List.of() : doctor.getWorkSlots().stream().filter(s -> s.getWeekday() == weekday).toList();
    if (slots.isEmpty()) {
      throw new ConflictException("Doctor not available on selected day");
    }
    LocalTime end = time.plusMinutes(duration);
    boolean inSlot = slots.stream().anyMatch(s -> !time.isBefore(s.getStartTime()) && !end.isAfter(s.getEndTime()));
    if (!inSlot) {
      throw new ConflictException("Appointment outside doctor's work slot");
    }
  }

  private void checkConflict(String doctorId, LocalDate date, LocalTime time, int duration, String excludeAppointmentId) {
    LocalTime end = time.plusMinutes(duration);
    List<Appointment> sameDay = appointmentRepository.findByDoctorIdAndDateAndStatusIn(doctorId, date, List.of(AppointmentStatus.Scheduled));
    for (Appointment a : sameDay) {
      if (excludeAppointmentId != null && excludeAppointmentId.equals(a.getId())) {
        continue;
      }
      LocalTime aStart = a.getTime();
      LocalTime aEnd = a.getTime().plusMinutes(a.getDurationMinutes());
      if (time.isBefore(aEnd) && end.isAfter(aStart)) {
        throw new ConflictException("Appointment time conflicts with existing appointment");
      }
    }
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void autoTerminatePastAppointments() {
    LocalDate today = LocalDate.now();
    List<Appointment> list = appointmentRepository.findAll();
    List<Appointment> updates = new ArrayList<>();
    for (Appointment a : list) {
      if (a.getStatus() == AppointmentStatus.Scheduled && a.getDate().isBefore(today)) {
        a.setStatus(AppointmentStatus.Terminated);
        a.setUpdatedAt(Instant.now());
        updates.add(a);
      }
    }
    if (!updates.isEmpty()) {
      appointmentRepository.saveAll(updates);
      log.info("Auto-terminated {} past appointments", updates.size());
    }
  }

  private List<Appointment> filterByType(List<Appointment> list, String type) {
    if (type == null || type.isBlank()) {
      return list;
    }
    LocalDate today = LocalDate.now();
    if ("upcoming".equalsIgnoreCase(type)) {
      return list.stream().filter(a -> a.getStatus() == AppointmentStatus.Scheduled && (a.getDate().isAfter(today) || a.getDate().isEqual(today))).toList();
    }
    if ("past".equalsIgnoreCase(type)) {
      return list.stream().filter(a -> a.getDate().isBefore(today)).toList();
    }
    return list;
  }
}