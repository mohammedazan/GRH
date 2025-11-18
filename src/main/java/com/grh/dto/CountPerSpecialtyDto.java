package com.grh.dto;

public class CountPerSpecialtyDto {
  private String specialty;
  private long count;

  public CountPerSpecialtyDto() {
  }

  public CountPerSpecialtyDto(String specialty, long count) {
    this.specialty = specialty;
    this.count = count;
  }

  public String getSpecialty() {
    return specialty;
  }

  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }
}