package com.grh.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grh.dto.AppointmentResponse;
import com.grh.dto.CountPerDoctorDto;
import com.grh.dto.CountPerSpecialtyDto;
import com.grh.dto.FrequentPatientDto;
import com.grh.service.ReportsService;

@RestController
@RequestMapping("/reports")
@Validated
public class ReportsController {
  private final ReportsService reportsService;

  public ReportsController(ReportsService reportsService) {
    this.reportsService = reportsService;
  }

  @GetMapping("/day")
  public ResponseEntity<List<AppointmentResponse>> appointmentsOfDay(
      @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return ResponseEntity.ok(reportsService.appointmentsOfDay(date));
  }

  @GetMapping("/appointments-per-doctor")
  public ResponseEntity<List<CountPerDoctorDto>> appointmentsPerDoctor() {
    return ResponseEntity.ok(reportsService.appointmentsPerDoctor());
  }

  @GetMapping("/appointments-per-specialty")
  public ResponseEntity<List<CountPerSpecialtyDto>> appointmentsPerSpecialty() {
    return ResponseEntity.ok(reportsService.appointmentsPerSpecialty());
  }

  @GetMapping("/frequent-patients")
  public ResponseEntity<List<FrequentPatientDto>> frequentPatients(
      @RequestParam(value = "days", defaultValue = "60") int days) {
    return ResponseEntity.ok(reportsService.frequentPatients(days));
  }
}