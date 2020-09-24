package com.haulmont.testtask.components;

import com.haulmont.testtask.model.Patient;
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
public class PatientEditor extends VerticalLayout implements KeyNotifier {
    private final PatientRepository patientRepository;

    private Patient patient;

    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private TextField patronymic = new TextField("Patronymic");
    private TextField phone = new TextField("Phone");

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private Binder<Patient> binder = new Binder<>(Patient.class);
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public PatientEditor(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;

        name.setRequired(true);
        surname.setRequired(true);
        patronymic.setRequired(true);
        phone.setRequired(true);

        add(name, surname, patronymic, phone, actions);

        binder.bindInstanceFields(this);
        binder.forField(name)
                .asRequired("Recipe must have Name")
                .bind(Patient::getName, Patient::setName);
        binder.forField(surname)
                .asRequired("Recipe must have Surname")
                .bind(Patient::getSurname, Patient::setSurname);
        binder.forField(patronymic)
                .asRequired("Recipe must have Patronymic")
                .bind(Patient::getPatronymic, Patient::setPatronymic);
        binder.forField(phone)
                .asRequired("Recipe must have Phone")
                .bind(Patient::getPhone, Patient::setPhone);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editPatient(patient));
        setVisible(false);
    }

    private void delete() {
        patientRepository.delete(patient);
        changeHandler.onChange();
    }

    private void save() {
        if (binder.validate().hasErrors()){
            return;
        }
        patientRepository.save(patient);
        changeHandler.onChange();
    }

    public void editPatient (Patient patientNew) {
        if (patientNew == null){
            setVisible(false);
            return;
        }

        if (java.util.Objects.nonNull(patientNew.getId())){
            patient = patientRepository.findById(patientNew.getId()).orElse(patientNew);
        } else {
            patient = patientNew;
        }

        binder.setBean(patient);

        setVisible(true);

        name.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
