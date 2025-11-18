package com.grh.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grh.dto.DoctorRequest;
import com.grh.dto.DoctorResponse;
import com.grh.dto.TimeIntervalDto;
import com.grh.service.DoctorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/doctors")
@Validated
public class DoctorController {
  private final DoctorService doctorService;

  public DoctorController(DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  @PostMapping
  public ResponseEntity<DoctorResponse> create(@Valid @RequestBody DoctorRequest request) {
    DoctorResponse r = doctorService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(r);
  }

  @GetMapping
  public ResponseEntity<List<DoctorResponse>> getAll() {
    return ResponseEntity.ok(doctorService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<DoctorResponse> getById(@PathVariable String id) {
    return ResponseEntity.ok(doctorService.getById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<DoctorResponse> update(@PathVariable String id, @Valid @RequestBody DoctorRequest request) {
    return ResponseEntity.ok(doctorService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    doctorService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/search")
  public ResponseEntity<List<DoctorResponse>> search(@RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "specialty", required = false) String specialty) {
    return ResponseEntity.ok(doctorService.search(name, specialty));
  }

  @GetMapping("/{id}/available-slots")
  public ResponseEntity<List<TimeIntervalDto>> availableSlots(@PathVariable String id,
      @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return ResponseEntity.ok(doctorService.getAvailableSlots(id, date));
  }
}