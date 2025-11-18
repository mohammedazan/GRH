package com.grh.mapper;

import java.util.ArrayList;
import java.util.List;

import com.grh.dto.DoctorRequest;
import com.grh.dto.DoctorResponse;
import com.grh.dto.WorkSlotDto;
import com.grh.model.Doctor;
import com.grh.model.WorkSlot;

public class DoctorMapper {
  public static Doctor toEntity(DoctorRequest request) {
    Doctor d = new Doctor();
    d.setFullName(request.getFullName());
    d.setSpecialty(request.getSpecialty());
    d.setPhone(request.getPhone());
    d.setEmail(request.getEmail());
    List<WorkSlot> slots = new ArrayList<>();
    if (request.getWorkSlots() != null) {
      for (WorkSlotDto s : request.getWorkSlots()) {
        slots.add(new WorkSlot(s.getWeekday(), s.getStartTime(), s.getEndTime()));
      }
    }
    d.setWorkSlots(slots);
    return d;
  }

  public static void updateEntity(Doctor d, DoctorRequest request) {
    d.setFullName(request.getFullName());
    d.setSpecialty(request.getSpecialty());
    d.setPhone(request.getPhone());
    d.setEmail(request.getEmail());
    List<WorkSlot> slots = new ArrayList<>();
    if (request.getWorkSlots() != null) {
      for (WorkSlotDto s : request.getWorkSlots()) {
        slots.add(new WorkSlot(s.getWeekday(), s.getStartTime(), s.getEndTime()));
      }
    }
    d.setWorkSlots(slots);
  }

  public static DoctorResponse toResponse(Doctor d) {
    DoctorResponse r = new DoctorResponse();
    r.setId(d.getId());
    r.setFullName(d.getFullName());
    r.setSpecialty(d.getSpecialty());
    r.setPhone(d.getPhone());
    r.setEmail(d.getEmail());
    List<WorkSlotDto> slots = new ArrayList<>();
    if (d.getWorkSlots() != null) {
      for (WorkSlot s : d.getWorkSlots()) {
        slots.add(new WorkSlotDto(){
          {
            setWeekday(s.getWeekday());
            setStartTime(s.getStartTime());
            setEndTime(s.getEndTime());
          }
        });
      }
    }
    r.setWorkSlots(slots);
    r.setCreatedAt(d.getCreatedAt());
    r.setUpdatedAt(d.getUpdatedAt());
    return r;
  }
}