package com.grh.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.grh.dto.DoctorRequest;
import com.grh.dto.DoctorResponse;
import com.grh.dto.TimeIntervalDto;
import com.grh.dto.WorkSlotDto;
import com.grh.exception.BadRequestException;
import com.grh.exception.NotFoundException;
import com.grh.mapper.DoctorMapper;
import com.grh.model.Appointment;
import com.grh.model.AppointmentStatus;
import com.grh.model.Doctor;
import com.grh.model.WorkSlot;
import com.grh.repository.AppointmentRepository;
import com.grh.repository.DoctorRepository;

@Service
public class DoctorService {
  private static final Logger log = LoggerFactory.getLogger(DoctorService.class);
  private final DoctorRepository doctorRepository;
  private final AppointmentRepository appointmentRepository;

  public DoctorService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
    this.doctorRepository = doctorRepository;
    this.appointmentRepository = appointmentRepository;
  }

  public DoctorResponse create(DoctorRequest request) {
    validateWorkSlots(request.getWorkSlots());
    Doctor d = DoctorMapper.toEntity(request);
    var now = java.time.Instant.now();
    d.setCreatedAt(now);
    d.setUpdatedAt(now);
    d = doctorRepository.save(d);
    log.info("Doctor created: {}", d.getId());
    return DoctorMapper.toResponse(d);
  }

  public DoctorResponse update(String id, DoctorRequest request) {
    validateWorkSlots(request.getWorkSlots());
    Doctor d = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor not found"));
    DoctorMapper.updateEntity(d, request);
    d.setUpdatedAt(java.time.Instant.now());
    d = doctorRepository.save(d);
    log.info("Doctor updated: {}", d.getId());
    return DoctorMapper.toResponse(d);
  }

  private void validateWorkSlots(List<WorkSlotDto> slots) {
    if (slots == null || slots.isEmpty()) {
      throw new BadRequestException("At least one work slot is required");
    }
    for (WorkSlotDto s : slots) {
      if (s.getStartTime() == null || s.getEndTime() == null || !s.getStartTime().isBefore(s.getEndTime())) {
        throw new BadRequestException("Invalid work slot time range");
      }
      if (s.getWeekday() < 1 || s.getWeekday() > 7) {
        throw new BadRequestException("Invalid weekday");
      }
    }
  }

  public void delete(String id) {
    Optional<Doctor> d = doctorRepository.findById(id);
    if (d.isEmpty()) {
      throw new NotFoundException("Doctor not found");
    }
    doctorRepository.deleteById(id);
    log.info("Doctor deleted: {}", id);
  }

  public DoctorResponse getById(String id) {
    Doctor d = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor not found"));
    return DoctorMapper.toResponse(d);
  }

  public List<DoctorResponse> getAll() {
    return doctorRepository.findAll().stream().map(DoctorMapper::toResponse).toList();
  }

  public List<DoctorResponse> search(String name, String specialty) {
    List<Doctor> list;
    if (name != null && !name.isBlank() && specialty != null && !specialty.isBlank()) {
      List<Doctor> byName = doctorRepository.findByFullNameContainingIgnoreCase(name);
      list = byName.stream().filter(d -> d.getSpecialty() != null && d.getSpecialty().toLowerCase().contains(specialty.toLowerCase())).toList();
    } else if (name != null && !name.isBlank()) {
      list = doctorRepository.findByFullNameContainingIgnoreCase(name);
    } else if (specialty != null && !specialty.isBlank()) {
      list = doctorRepository.findBySpecialtyContainingIgnoreCase(specialty);
    } else {
      list = doctorRepository.findAll();
    }
    return list.stream().map(DoctorMapper::toResponse).toList();
  }

  public List<TimeIntervalDto> getAvailableSlots(String doctorId, LocalDate date) {
    Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new NotFoundException("Doctor not found"));
    int weekday = date.getDayOfWeek().getValue();
    List<WorkSlot> daySlots = doctor.getWorkSlots() == null ? List.of() : doctor.getWorkSlots().stream().filter(s -> s.getWeekday() == weekday).toList();
    List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDateAndStatusIn(doctorId, date, List.of(AppointmentStatus.Scheduled));
    List<TimeIntervalDto> free = new ArrayList<>();
    for (WorkSlot slot : daySlots) {
      LocalTime current = slot.getStartTime();
      LocalTime end = slot.getEndTime();
      List<Appointment> overlapping = appointments.stream()
          .filter(a -> overlaps(slot.getStartTime(), slot.getEndTime(), a.getTime(), a.getTime().plusMinutes(a.getDurationMinutes())))
          .sorted(Comparator.comparing(Appointment::getTime))
          .toList();
      for (Appointment a : overlapping) {
        LocalTime aStart = a.getTime();
        LocalTime aEnd = a.getTime().plusMinutes(a.getDurationMinutes());
        if (aStart.isAfter(current)) {
          free.add(new TimeIntervalDto(current, aStart));
        }
        if (aEnd.isAfter(current)) {
          current = aEnd.isAfter(end) ? end : aEnd;
        }
      }
      if (current.isBefore(end)) {
        free.add(new TimeIntervalDto(current, end));
      }
    }
    return free;
  }

  private boolean overlaps(LocalTime s1, LocalTime e1, LocalTime s2, LocalTime e2) {
    return s2.isBefore(e1) && e2.isAfter(s1);
  }
}