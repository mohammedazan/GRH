package com.grh.mapper;

import com.grh.dto.PatientRequest;
import com.grh.dto.PatientResponse;
import com.grh.model.Patient;

public class PatientMapper {
  public static Patient toEntity(PatientRequest request) {
    Patient p = new Patient();
    p.setFullName(request.getFullName());
    p.setDateOfBirth(request.getDateOfBirth());
    p.setGender(request.getGender());
    p.setPhone(request.getPhone());
    p.setEmail(request.getEmail());
    p.setAddress(request.getAddress());
    return p;
  }

  public static void updateEntity(Patient p, PatientRequest request) {
    p.setFullName(request.getFullName());
    p.setDateOfBirth(request.getDateOfBirth());
    p.setGender(request.getGender());
    p.setPhone(request.getPhone());
    p.setEmail(request.getEmail());
    p.setAddress(request.getAddress());
  }

  public static PatientResponse toResponse(Patient p) {
    PatientResponse r = new PatientResponse();
    r.setId(p.getId());
    r.setFullName(p.getFullName());
    r.setDateOfBirth(p.getDateOfBirth());
    r.setGender(p.getGender());
    r.setPhone(p.getPhone());
    r.setEmail(p.getEmail());
    r.setAddress(p.getAddress());
    r.setCreatedAt(p.getCreatedAt());
    r.setUpdatedAt(p.getUpdatedAt());
    return r;
  }
}