package com.interviewcoach.model;
import jakarta.persistence.*;import java.time.Instant;
@Entity @Table(name="users") public class User {@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id; public String name;@Column(unique=true)public String email;@Column(name="password_hash")public String passwordHash;@Column(name="target_role")public String targetRole="Software Engineer";@Column(name="created_at")public Instant createdAt=Instant.now();}
