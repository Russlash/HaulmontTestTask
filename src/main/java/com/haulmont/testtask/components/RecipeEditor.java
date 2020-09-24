package com.haulmont.testtask.components;

import com.haulmont.testtask.model.Doctor;
import com.haulmont.testtask.model.Patient;
import com.haulmont.testtask.model.Priority;
import com.haulmont.testtask.model.Recipe;
import com.haulmont.testtask.repository.DoctorRepository;
import com.haulmont.testtask.repository.PatientRepository;
import com.haulmont.testtask.repository.PriorityRepository;
import com.haulmont.testtask.repository.RecipeRepository;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SpringComponent
@UIScope
public class RecipeEditor extends VerticalLayout implements KeyNotifier {
    private final RecipeRepository recipeRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PriorityRepository priorityRepository;

    private Recipe recipe;

    private TextField description = new TextField("Description");
    private ComboBox<Patient> patientComboBox = new ComboBox<>();
    private ComboBox<Doctor> doctorComboBox = new ComboBox<>();
    private DatePicker dateCreationPicker = new DatePicker();
    private DatePicker dateValidationPicker = new DatePicker();
    private ComboBox<Priority> priorityComboBox = new ComboBox<>();

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private Binder<Recipe> binder = new Binder<>(Recipe.class);
    private RecipeEditor.ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public RecipeEditor(RecipeRepository recipeRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, PriorityRepository priorityRepository) {
        this.recipeRepository = recipeRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.priorityRepository = priorityRepository;

        description.setRequired(true);
        dateCreationPicker.setLabel("Date Creation");
        dateCreationPicker.setRequired(true);
        dateValidationPicker.setLabel("Date Validation");
        dateValidationPicker.setRequired(true);
        patientComboBox.setLabel("Patient");
        patientComboBox.setItemLabelGenerator(patient -> patient.getName() + " " + patient.getSurname() + " " + patient.getPatronymic());
        patientComboBox.setRequired(true);
        doctorComboBox.setLabel("Doctor");
        doctorComboBox.setItemLabelGenerator(doctor -> doctor.getName() + " " + doctor.getSurname() + " " + doctor.getPatronymic());
        doctorComboBox.setRequired(true);
        priorityComboBox.setLabel("Priority");
        priorityComboBox.setItemLabelGenerator(Priority::getName);
        priorityComboBox.setRequired(true);

        add(description, patientComboBox, doctorComboBox, dateCreationPicker, dateValidationPicker, priorityComboBox, actions);

        binder.bindInstanceFields(this);
        binder.forField(description)
                .asRequired("Recipe must have Description")
                .bind(Recipe::getDescription, Recipe::setDescription);
        binder.forField(patientComboBox)
                .asRequired("Recipe must have Patient")
                .bind(Recipe::getPatient, Recipe::setPatient);
        binder.forField(doctorComboBox)
                .asRequired("Recipe must have Doctor")
                .bind(Recipe::getDoctor, Recipe::setDoctor);
        binder.forField(priorityComboBox)
                .asRequired("Recipe must have Priority")
                .bind(Recipe::getPriority, Recipe::setPriority);
        binder.forField(dateCreationPicker)
                .asRequired("Recipe must have correct Date Creation")
                .bind(Recipe::getDateCreation, Recipe::setDateCreation);
        binder.forField(dateValidationPicker)
                .asRequired("Recipe must have correct Date Validation")
                .bind(Recipe::getValidity, Recipe::setValidity);

        setSpacing(true);


        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editRecipe(recipe));
        setVisible(false);
    }

    private void delete() {
        recipeRepository.delete(recipe);
        changeHandler.onChange();
    }

    private void save() {
        if (binder.validate().hasErrors()){
            return;
        }
        recipeRepository.save(recipe);
        changeHandler.onChange();
    }

    public void editRecipe (Recipe recipeNew) {
        if (recipeNew == null){
            setVisible(false);
            return;
        }

        if (java.util.Objects.nonNull(recipeNew.getId())){
            recipe = recipeRepository.findById(recipeNew.getId()).orElse(recipeNew);
        } else {
            recipe = recipeNew;
        }

        binder.setBean(recipe);

        setVisible(true);

        description.focus();
    }

    public void setChangeHandler(RecipeEditor.ChangeHandler h) {
        changeHandler = h;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        List<Patient> patients = patientRepository.findAll();
        List<Doctor> doctors = doctorRepository.findAll();
        List<Priority> priorities = priorityRepository.findAll();
        patientComboBox.setItems(patients);
        doctorComboBox.setItems(doctors);
        priorityComboBox.setItems(priorities);
    }


}
