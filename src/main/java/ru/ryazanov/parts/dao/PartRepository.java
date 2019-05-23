package ru.ryazanov.parts.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ryazanov.parts.model.Part;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part, Integer> {
    List<Part> findByNameContaining(String name);

    List<Part> findByIsRequiredTrue();
}
