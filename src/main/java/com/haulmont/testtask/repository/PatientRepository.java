package com.haulmont.testtask.repository;

import com.haulmont.testtask.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
