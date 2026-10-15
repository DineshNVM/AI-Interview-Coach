package com.interviewcoach.model;
import jakarta.persistence.*;import java.time.Instant;
@Entity @Table(name="proctor_events") public class ProctorEvent {@Id @GeneratedValue(strategy=GenerationType.IDENTITY)public Long id;@ManyToOne(fetch=FetchType.LAZY)@JoinColumn(name="interview_id")public Interview interview;public String category,severity;@Column(name="occurred_at")public Instant occurredAt=Instant.now();@Column(name="duration_seconds")public Integer durationSeconds;public String detail;}
