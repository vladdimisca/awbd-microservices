package com.awbd.jobservice.service;

import com.awbd.jobservice.model.Job;

import java.util.List;

public interface JobService {
    Job create(Job job);

    Job update(Long id, Job job);

    Job getById(Long id);

    List<Job> getAll();

    void deleteById(Long id);
}
