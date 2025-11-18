package com.grh.model;

import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class WorkSlot {
  private int weekday;
  @JsonFormat(pattern = "HH:mm")
  private LocalTime startTime;
  @JsonFormat(pattern = "HH:mm")
  private LocalTime endTime;

  public WorkSlot() {
  }

  public WorkSlot(int weekday, LocalTime startTime, LocalTime endTime) {
    this.weekday = weekday;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public int getWeekday() {
    return weekday;
  }

  public void setWeekday(int weekday) {
    this.weekday = weekday;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
  }
}