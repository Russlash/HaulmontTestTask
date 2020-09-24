package com.haulmont.testtask.repository;

import com.haulmont.testtask.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("FROM Recipe AS r " +
            "JOIN r.patient AS p " +
            "JOIN r.priority AS pr " +
            "WHERE p.name like %:searchString% or p.surname like %:searchString% or p.patronymic like %:searchString% " +
            "or pr.name like %:searchString% " +
            "or r.description like %:searchString%")
    List<Recipe> search(String searchString);

}
