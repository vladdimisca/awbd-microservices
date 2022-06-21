package com.awbd.appointmentservice.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private JobType type;

    @Column(name = "price")
    private Double price;

    @Column(name = "duration_minutes")
    private Long durationMinutes;

    @Column(name = "car_type")
    @Enumerated(EnumType.STRING)
    private CarType carType;

    @Column(name = "number_of_employees")
    private Integer numberOfEmployees;

    @OneToMany(mappedBy = "job", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Appointment> appointments;
}
