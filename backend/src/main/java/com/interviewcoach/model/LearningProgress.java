package com.interviewcoach.model;
import jakarta.persistence.*;
@Entity @Table(name="learning_progress") public class LearningProgress {@Id @GeneratedValue(strategy=GenerationType.IDENTITY)public Long id;@ManyToOne(fetch=FetchType.LAZY)@JoinColumn(name="user_id")public User user;@Column(name="resource_id")public String resourceId;public String status;}
