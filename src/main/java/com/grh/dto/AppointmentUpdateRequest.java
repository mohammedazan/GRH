package com.grh.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Min;

public class AppointmentUpdateRequest {
  private LocalDate date;
  @JsonFormat(pattern = "HH:mm")
  private LocalTime time;
  @Min(1)
  private Integer durationMinutes;

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public LocalTime getTime() {
    return time;
  }

  public void setTime(LocalTime time) {
    this.time = time;
  }

  public Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }
}