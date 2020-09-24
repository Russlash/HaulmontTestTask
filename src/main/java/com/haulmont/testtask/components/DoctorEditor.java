package com.haulmont.testtask.components;

import com.haulmont.testtask.model.Doctor;
import com.haulmont.testtask.model.Patient;
import com.haulmont.testtask.repository.DoctorRepository;
import com.haulmont.testtask.repository.PatientRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class DoctorEditor extends VerticalLayout implements KeyNotifier {
    private final DoctorRepository doctorRepository;

    private Doctor doctor;


    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private TextField patronymic = new TextField("Patronymic");
    private TextField specialization = new TextField("Specialization");

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private Binder<Doctor> binder = new Binder<>(Doctor.class);
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public DoctorEditor(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;

        name.setRequired(true);
        surname.setRequired(true);
        patronymic.setRequired(true);
        specialization.setRequired(true);

        add(name, surname, patronymic, specialization, actions);

        binder.bindInstanceFields(this);
        binder.forField(name)
                .asRequired("Recipe must have Name")
                .bind(Doctor::getName, Doctor::setName);
        binder.forField(surname)
                .asRequired("Recipe must have Surname")
                .bind(Doctor::getSurname, Doctor::setSurname);
        binder.forField(patronymic)
                .asRequired("Recipe must have Patronymic")
                .bind(Doctor::getPatronymic, Doctor::setPatronymic);
        binder.forField(specialization)
                .asRequired("Recipe must have Specialization")
                .bind(Doctor::getSpecialization, Doctor::setSpecialization);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editDoctor(doctor));
        setVisible(false);
    }

    private void delete() {
        doctorRepository.delete(doctor);
        changeHandler.onChange();
    }

    private void save() {
        if (binder.validate().hasErrors()){
            return;
        }
        doctorRepository.save(doctor);
        changeHandler.onChange();
    }

    public void editDoctor (Doctor doctorNew) {
        if (doctorNew == null){
            setVisible(false);
            return;
        }

        if (java.util.Objects.nonNull(doctorNew.getId())){
            doctor = doctorRepository.findById(doctorNew.getId()).orElse(doctorNew);
        } else {
            doctor = doctorNew;
        }

        binder.setBean(doctor);

        setVisible(true);

        name.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
