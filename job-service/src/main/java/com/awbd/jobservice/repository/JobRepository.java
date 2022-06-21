package com.awbd.jobservice.repository;

import com.awbd.jobservice.model.CarType;
import com.awbd.jobservice.model.Job;
import com.awbd.jobservice.model.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    boolean existsByTypeAndCarType(JobType type, CarType carType);
}
