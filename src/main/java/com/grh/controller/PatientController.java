package com.grh.controller;

import java.util.List;

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

import com.grh.dto.PatientRequest;
import com.grh.dto.PatientResponse;
import com.grh.service.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/patients")
@Validated
public class PatientController {
  private final PatientService patientService;

  public PatientController(PatientService patientService) {
    this.patientService = patientService;
  }

  @PostMapping
  public ResponseEntity<PatientResponse> create(@Valid @RequestBody PatientRequest request) {
    PatientResponse r = patientService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(r);
  }

  @GetMapping
  public ResponseEntity<List<PatientResponse>> getAll(@RequestParam(value = "name", required = false) String name) {
    return ResponseEntity.ok(patientService.getAll(name));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PatientResponse> getById(@PathVariable String id) {
    return ResponseEntity.ok(patientService.getById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PatientResponse> update(@PathVariable String id, @Valid @RequestBody PatientRequest request) {
    return ResponseEntity.ok(patientService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    patientService.delete(id);
    return ResponseEntity.noContent().build();
  }
}