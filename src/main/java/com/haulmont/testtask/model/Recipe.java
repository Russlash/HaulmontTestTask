package com.haulmont.testtask.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "Description", nullable = false)
    private String description;

    @ManyToOne(cascade= CascadeType.MERGE)
    @JoinColumn (name = "Patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(cascade= CascadeType.MERGE)
    @JoinColumn(name = "Doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "Date_creation", nullable = false)
    private LocalDate dateCreation;

    @Column(name = "Validity", nullable = false)
    private LocalDate validity;

    @ManyToOne(cascade= CascadeType.MERGE)
    @JoinColumn(name = "Priority_id", nullable = false)
    private Priority priority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getValidity() {
        return validity;
    }

    public void setValidity(LocalDate validity) {
        this.validity = validity;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
