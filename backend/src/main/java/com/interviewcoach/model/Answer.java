package com.interviewcoach.model;
import jakarta.persistence.*;import java.time.Instant;
@Entity @Table(name="answers") public class Answer {@Id @GeneratedValue(strategy=GenerationType.IDENTITY)public Long id;@ManyToOne(fetch=FetchType.LAZY)@JoinColumn(name="interview_id")public Interview interview;public int position;@Column(columnDefinition="text")public String question;@Column(columnDefinition="text")public String answer;@Column(name="created_at")public Instant createdAt=Instant.now();}
