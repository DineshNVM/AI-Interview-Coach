package com.interviewcoach.model;
import jakarta.persistence.*;import java.time.Instant;
@Entity @Table(name="auth_tokens") public class AuthToken {@Id @GeneratedValue(strategy=GenerationType.IDENTITY)public Long id;@ManyToOne(fetch=FetchType.LAZY)@JoinColumn(name="user_id")public User user;@Column(name="token_hash")public String tokenHash;@Column(name="expires_at")public Instant expiresAt;}
