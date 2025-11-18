package com.grh.dto;

public class FrequentPatientDto {
  private String patientId;
  private long count;

  public FrequentPatientDto() {
  }

  public FrequentPatientDto(String patientId, long count) {
    this.patientId = patientId;
    this.count = count;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }
}