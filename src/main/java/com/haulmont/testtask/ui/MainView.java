package com.haulmont.testtask.ui;

import com.haulmont.testtask.components.PatientEditor;
import com.haulmont.testtask.model.Patient;
import com.haulmont.testtask.repository.PatientRepository;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class MainView extends VerticalLayout {
    private final PatientRepository patientRepository;

    private final Grid<Patient> grid = new Grid<>(Patient.class);

    private final H3 labelListPatient = new H3("List of Patient");
    private final Button addNewBtn = new Button("Add new");
    private final Button btnDoctor = new Button("List of doctors");
    private final Button btnRecipe = new Button("List of recipes");
    private final Div menu = new Div();
    private final HorizontalLayout toolbar = new HorizontalLayout(addNewBtn);

    private final PatientEditor editor;

    @Autowired
    public MainView (PatientRepository patientRepository, PatientEditor editor) {
        this.patientRepository = patientRepository;
        this.editor = editor;

        btnDoctor.addClickListener(e ->
                btnDoctor.getUI().ifPresent(ui ->
                        ui.navigate("doctor"))
        );
        btnRecipe.addClickListener(e ->
                btnRecipe.getUI().ifPresent(ui ->
                        ui.navigate("recipe"))
        );

        menu.add(btnDoctor);
        menu.add(btnRecipe);

        add(menu, toolbar, labelListPatient, grid, editor);

        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editPatient(e.getValue());
        });

        addNewBtn.addClickListener(e -> editor.editPatient(new Patient()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            grid.setItems(patientRepository.findAll());
        });

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        grid.setItems(patientRepository.findAll());
    }
}
