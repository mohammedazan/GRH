package com.grh.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grh.dto.AppointmentCreateRequest;
import com.grh.dto.AppointmentResponse;
import com.grh.dto.AppointmentUpdateRequest;
import com.grh.service.AppointmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointments")
@Validated
public class AppointmentController {
  private final AppointmentService appointmentService;

  public AppointmentController(AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  @PostMapping
  public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody AppointmentCreateRequest request) {
    AppointmentResponse r = appointmentService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(r);
  }

  @GetMapping
  public ResponseEntity<List<AppointmentResponse>> getAll(@RequestParam(value = "type", required = false) String type) {
    return ResponseEntity.ok(appointmentService.getAll(type));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AppointmentResponse> getById(@PathVariable String id) {
    return ResponseEntity.ok(appointmentService.getById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<AppointmentResponse> update(@PathVariable String id, @Valid @RequestBody AppointmentUpdateRequest request) {
    return ResponseEntity.ok(appointmentService.update(id, request));
  } 

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    appointmentService.cancel(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/day")
  public ResponseEntity<List<AppointmentResponse>> getByDay(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return ResponseEntity.ok(appointmentService.getByDay(date));
  }

  @GetMapping("/doctor/{doctorId}")
  public ResponseEntity<List<AppointmentResponse>> getByDoctor(@PathVariable String doctorId) {
    return ResponseEntity.ok(appointmentService.getByDoctor(doctorId));
  }

  @GetMapping("/patient/{patientId}")
  public ResponseEntity<List<AppointmentResponse>> getByPatient(@PathVariable String patientId) {
    return ResponseEntity.ok(appointmentService.getByPatient(patientId));
  }
}