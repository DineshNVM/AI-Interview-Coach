package com.interviewcoach.model;
import jakarta.persistence.*;import java.time.Instant;
@Entity @Table(name="resumes") public class Resume {@Id @GeneratedValue(strategy=GenerationType.IDENTITY)public Long id;@ManyToOne(fetch=FetchType.LAZY)@JoinColumn(name="user_id")public User user;public String filename;@Column(name="stored_name")public String storedName;@Column(name="extracted_text",columnDefinition="text")public String extractedText;@Column(columnDefinition="text")public String analysis;@Column(name="created_at")public Instant createdAt=Instant.now();}
