package ru.ryazanov.parts.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.ryazanov.parts.model.Part;

import java.util.List;

public interface PartService{
    void save(Part part);

    List<Part> findAll();

    Part getPartById(int id);

    void updatePart(Part newPart);

    void deletePartById(int id);

    List<Part> getPartsByNameContaining(String filter);

    int getRequiredCollect();

    Page<Part> findPaginated(Pageable pageable, List<Part> partList);
}

