package com.grh.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.grh.dto.PatientRequest;
import com.grh.dto.PatientResponse;
import com.grh.exception.NotFoundException;
import com.grh.mapper.PatientMapper;
import com.grh.model.Patient;
import com.grh.repository.PatientRepository;
/*
 * PatientService
مسؤول على:
createPatient
updatePatient
deletePatient
getById
searchByName
يستعمل:
PatientRepository
PatientMapper
فيه Logs باش تقدر تشوف فـ console شنو كيوقع.
 */

@Service
public class PatientService {
  private static final Logger log = LoggerFactory.getLogger(PatientService.class);
  private final PatientRepository patientRepository;

  public PatientService(PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
  }

  public PatientResponse create(PatientRequest request) {
    Patient patient = PatientMapper.toEntity(request);
    Instant now = Instant.now();
    patient.setCreatedAt(now);
    patient.setUpdatedAt(now);
    patient = patientRepository.save(patient);
    log.info("Patient created: {}", patient.getId());
    return PatientMapper.toResponse(patient);
  }

  public PatientResponse update(String id, PatientRequest request) {
    Patient patient = patientRepository.findById(id).orElseThrow(() -> new NotFoundException("Patient not found"));
    PatientMapper.updateEntity(patient, request);
    patient.setUpdatedAt(Instant.now());
    patient = patientRepository.save(patient);
    log.info("Patient updated: {}", patient.getId());
    return PatientMapper.toResponse(patient);
  }

  public void delete(String id) {
    Optional<Patient> p = patientRepository.findById(id);
    if (p.isEmpty()) {
      throw new NotFoundException("Patient not found");
    }
    patientRepository.deleteById(id);
    log.info("Patient deleted: {}", id);
  }

  public PatientResponse getById(String id) {
    Patient patient = patientRepository.findById(id).orElseThrow(() -> new NotFoundException("Patient not found"));
    return PatientMapper.toResponse(patient);
  }

  public List<PatientResponse> getAll(String name) {
    List<Patient> list;
    if (name != null && !name.isBlank()) {
      list = patientRepository.findByFullNameContainingIgnoreCase(name);
    } else {
      list = patientRepository.findAll();
    }
    return list.stream().map(PatientMapper::toResponse).toList();
  }
}