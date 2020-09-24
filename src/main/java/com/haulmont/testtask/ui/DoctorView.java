package com.haulmont.testtask.ui;

import com.haulmont.testtask.components.DoctorEditor;
import com.haulmont.testtask.model.CountRecipeByDoctor;
import com.haulmont.testtask.model.Doctor;
import com.haulmont.testtask.model.Recipe;
import com.haulmont.testtask.repository.DoctorRepository;
import com.haulmont.testtask.repository.RecipeRepository;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route(value ="doctor")
public class DoctorView extends VerticalLayout {
    private final DoctorRepository doctorRepository;
    private final RecipeRepository recipeRepository;

    private final Grid<Doctor> grid = new Grid<>(Doctor.class);
    private final Grid<CountRecipeByDoctor> gridCount = new Grid<>(CountRecipeByDoctor.class);

    private final H3 labelListDoctor = new H3("List of Doctors");
    private final H3 labelListRecipeCount = new H3("Count recipes by doctor");
    private final Button addNewBtn = new Button("Add new");
    private final Button btnRecipe = new Button("List of recipes");
    private final Button btnPatient = new Button("List of patients");
    private final Div menu = new Div();
    private final HorizontalLayout toolbar = new HorizontalLayout(addNewBtn);

    private final DoctorEditor editor;

    @Autowired
    public DoctorView(DoctorRepository doctorRepository, RecipeRepository recipeRepository, DoctorEditor editor) {
        this.doctorRepository = doctorRepository;
        this.recipeRepository = recipeRepository;
        this.editor = editor;

        btnPatient.addClickListener(e ->
                btnPatient.getUI().ifPresent(ui ->
                        ui.navigate(""))
        );
        btnRecipe.addClickListener(e ->
                btnRecipe.getUI().ifPresent(ui ->
                        ui.navigate("recipe"))
        );

        menu.add(btnPatient);
        menu.add(btnRecipe);

        add(menu, toolbar, labelListDoctor, grid, labelListRecipeCount,gridCount, editor);

        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editDoctor(e.getValue());
        });

        addNewBtn.addClickListener(e -> editor.editDoctor(new Doctor()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            grid.setItems(doctorRepository.findAll());
            gridCount.setItems(countRecipeByDoctor(recipeRepository.findAll()));
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        grid.setItems(doctorRepository.findAll());
        gridCount.setItems(countRecipeByDoctor(recipeRepository.findAll()));
    }

    private List<CountRecipeByDoctor> countRecipeByDoctor(List<Recipe> recipes){
        Map<Doctor, Long> countRecipeByDoctorMap = recipes.stream()
                .collect(Collectors.groupingBy(Recipe::getDoctor, Collectors.counting()));
        List<CountRecipeByDoctor> countRecipeByDoctorList = new ArrayList<>();
        countRecipeByDoctorMap.forEach((doctor, aLong) -> countRecipeByDoctorList.add(new CountRecipeByDoctor(doctor.getName() + " " + doctor.getSurname() + " " + doctor.getPatronymic(), aLong)));
        return countRecipeByDoctorList;
    }
}
