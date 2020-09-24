package com.haulmont.testtask.repository;

import com.haulmont.testtask.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
