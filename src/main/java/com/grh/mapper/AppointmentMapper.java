package com.grh.mapper;

import com.grh.dto.AppointmentResponse;
import com.grh.model.Appointment;

public class AppointmentMapper {
  public static AppointmentResponse toResponse(Appointment a) {
    AppointmentResponse r = new AppointmentResponse();
    r.setId(a.getId());
    r.setPatientId(a.getPatientId());
    r.setDoctorId(a.getDoctorId());
    r.setDate(a.getDate());
    r.setTime(a.getTime());
    r.setDurationMinutes(a.getDurationMinutes());
    r.setStatus(a.getStatus());
    r.setCreatedAt(a.getCreatedAt());
    r.setUpdatedAt(a.getUpdatedAt());
    return r;
  }
}