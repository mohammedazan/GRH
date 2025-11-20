package com.grh.dto;

import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
//WorkSlotDto (باش تبعت workSlots للطبيب)
public class WorkSlotDto {
  @Min(1)
  @Max(7)
  private int weekday;

  @NotNull
  @JsonFormat(pattern = "HH:mm")
  private LocalTime startTime;

  @NotNull
  @JsonFormat(pattern = "HH:mm")
  private LocalTime endTime;

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