package com.grh.service;

import java.time.LocalDate;
import java.util.List;

import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.grh.dto.AppointmentResponse;
import com.grh.dto.CountPerDoctorDto;
import com.grh.dto.CountPerSpecialtyDto;
import com.grh.dto.FrequentPatientDto;
import com.grh.mapper.AppointmentMapper;
import com.grh.model.Appointment;

@Service
public class ReportsService {
  private final MongoTemplate mongoTemplate;

  public ReportsService(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public List<AppointmentResponse> appointmentsOfDay(LocalDate date) {
    MatchOperation match = Aggregation.match(Criteria.where("date").is(date));
    Aggregation agg = Aggregation.newAggregation(match, Aggregation.sort(Sort.by("time")));
    AggregationResults<Appointment> results = mongoTemplate.aggregate(agg, "appointments", Appointment.class);
    return results.getMappedResults().stream().map(AppointmentMapper::toResponse).toList();
  }

  public List<CountPerDoctorDto> appointmentsPerDoctor() {
    Aggregation agg = Aggregation.newAggregation(
        Aggregation.group("doctorId").count().as("count"),
        Aggregation.project().and("_id").as("doctorId").and("count").as("count"),
        Aggregation.sort(Sort.by(Sort.Direction.DESC, "count"))
    );
    AggregationResults<Document> results = mongoTemplate.aggregate(agg, "appointments", Document.class);
    return results.getMappedResults().stream()
        .map(d -> new CountPerDoctorDto(d.getString("doctorId"), d.get("count", Number.class).longValue()))
        .toList();
  }

  public List<CountPerSpecialtyDto> appointmentsPerSpecialty() {
    Aggregation agg = Aggregation.newAggregation(
        Aggregation.lookup("doctors", "doctorId", "_id", "doctor"),
        Aggregation.unwind("$doctor"),
        Aggregation.group("$doctor.specialty").count().as("count"),
        Aggregation.project().and("_id").as("specialty").and("count").as("count"),
        Aggregation.sort(Sort.by(Sort.Direction.DESC, "count"))
    );
    AggregationResults<Document> results = mongoTemplate.aggregate(agg, "appointments", Document.class);
    return results.getMappedResults().stream()
        .map(d -> new CountPerSpecialtyDto(d.getString("specialty"), d.get("count", Number.class).longValue()))
        .toList();
  }

  public List<FrequentPatientDto> frequentPatients(int days) {
    LocalDate from = LocalDate.now().minusDays(days);
    Aggregation agg = Aggregation.newAggregation(
        Aggregation.match(Criteria.where("date").gte(from)),
        Aggregation.group("patientId").count().as("count"),
        Aggregation.match(Criteria.where("count").gt(2)),
        Aggregation.project().and("_id").as("patientId").and("count").as("count"),
        Aggregation.sort(Sort.by(Sort.Direction.DESC, "count"))
    );
    AggregationResults<Document> results = mongoTemplate.aggregate(agg, "appointments", Document.class);
    return results.getMappedResults().stream()
        .map(d -> new FrequentPatientDto(d.getString("patientId"), d.get("count", Number.class).longValue()))
        .toList();
  }
}