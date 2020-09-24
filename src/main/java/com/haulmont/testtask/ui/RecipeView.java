package com.haulmont.testtask.ui;

import com.haulmont.testtask.components.RecipeEditor;
import com.haulmont.testtask.model.Recipe;
import com.haulmont.testtask.repository.RecipeRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route("recipe")
public class RecipeView extends VerticalLayout {
    private final RecipeRepository recipeRepository;

    private final Grid<Recipe> grid = new Grid<>(Recipe.class);

    private final H3 labelListRecipe = new H3("List of Recipes");
    private final TextField filter = new TextField("", "Type to filter");
    private final Button addNewBtn = new Button("Add new");
    private final Button btnDoctor = new Button("List of doctors");
    private final Button btnPatient = new Button("List of patients");
    private final Div menu = new Div();
    private final HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn);

    private final RecipeEditor editor;

    @Autowired
    public RecipeView(RecipeRepository recipeRepository, RecipeEditor editor) {
        this.recipeRepository = recipeRepository;
        this.editor = editor;

        btnDoctor.addClickListener(e ->
                btnDoctor.getUI().ifPresent(ui ->
                        ui.navigate("doctor"))
        );
        btnPatient.addClickListener(e ->
                btnPatient.getUI().ifPresent(ui ->
                        ui.navigate(""))
        );

        menu.add(btnDoctor);
        menu.add(btnPatient);

        grid.getColumnByKey("doctor").setVisible(false);
        grid.addColumn(recipe ->
                recipe.getDoctor().getName() + " " + recipe.getDoctor().getSurname() + " " + recipe.getDoctor().getPatronymic())
                .setHeader("Doctor");
        grid.getColumnByKey("patient").setVisible(false);
        grid.addColumn(recipe ->
                recipe.getPatient().getName() + " " + recipe.getPatient().getSurname() + " " + recipe.getPatient().getPatronymic())
                .setHeader("Patient");
        grid.getColumnByKey("priority").setVisible(false);
        grid.addColumn(recipe ->
                recipe.getPriority().getName())
                .setHeader("Priority");


        grid.getColumnByKey("dateCreation").setVisible(false);
        grid.addColumn(new LocalDateRenderer<>(
                (ValueProvider<Recipe, LocalDate>) Recipe::getDateCreation, "dd.MM.yyyy"))
                .setHeader("Date Creation");
        grid.getColumnByKey("validity").setVisible(false);
        grid.addColumn(new LocalDateRenderer<>(
                (ValueProvider<Recipe, LocalDate>) Recipe::getValidity, "dd.MM.yyyy"))
                .setHeader("Validity");

        add(menu, toolbar, labelListRecipe, grid, editor);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showRecipe(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editRecipe(e.getValue());
        });

        addNewBtn.addClickListener(e -> editor.editRecipe(new Recipe()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            showRecipe(filter.getValue());
        });

        showRecipe("");
    }

    private void showRecipe(String search) {
        if (search.isEmpty()) {
            grid.setItems(recipeRepository.findAll());
        } else {
            grid.setItems(recipeRepository.search(search));
        }
    }
}
