package com.grh.dto;

public class CountPerDoctorDto {
  private String doctorId;
  private long count;

  public CountPerDoctorDto() {
  }

  public CountPerDoctorDto(String doctorId, long count) {
    this.doctorId = doctorId;
    this.count = count;
  }

  public String getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(String doctorId) {
    this.doctorId = doctorId;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }
}