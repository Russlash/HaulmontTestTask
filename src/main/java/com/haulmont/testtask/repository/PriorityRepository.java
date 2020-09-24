package com.haulmont.testtask.repository;

import com.haulmont.testtask.model.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityRepository extends JpaRepository<Priority, Long> {
}
