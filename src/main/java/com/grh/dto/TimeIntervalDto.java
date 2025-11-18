package com.grh.dto;

import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class TimeIntervalDto {
  @JsonFormat(pattern = "HH:mm")
  private LocalTime start;
  @JsonFormat(pattern = "HH:mm")
  private LocalTime end;

  public TimeIntervalDto() {
  }

  public TimeIntervalDto(LocalTime start, LocalTime end) {
    this.start = start;
    this.end = end;
  }

  public LocalTime getStart() {
    return start;
  }

  public void setStart(LocalTime start) {
    this.start = start;
  }

  public LocalTime getEnd() {
    return end;
  }

  public void setEnd(LocalTime end) {
    this.end = end;
  }
}