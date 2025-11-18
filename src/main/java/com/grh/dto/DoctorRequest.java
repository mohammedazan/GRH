package com.grh.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class DoctorRequest {
  @NotBlank
  private String fullName;

  @NotBlank
  private String specialty;

  @NotBlank
  @Pattern(regexp = "^\\+?[0-9\\s]{7,15}$")
  private String phone;

  @NotBlank
  @Email
  private String email;

  @NotEmpty
  private List<WorkSlotDto> workSlots;

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getSpecialty() {
    return specialty;
  }

  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<WorkSlotDto> getWorkSlots() {
    return workSlots;
  }

  public void setWorkSlots(List<WorkSlotDto> workSlots) {
    this.workSlots = workSlots;
  }
}