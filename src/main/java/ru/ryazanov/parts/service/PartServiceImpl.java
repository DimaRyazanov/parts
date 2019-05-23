package ru.ryazanov.parts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ryazanov.parts.dao.PartRepository;
import ru.ryazanov.parts.model.Part;

import java.util.Collections;
import java.util.List;

@Service
public class PartServiceImpl implements PartService {

    @Autowired
    private PartRepository partRepository;

    @Override
    public void save(Part part) {
        partRepository.save(part);
    }

    @Override
    public List<Part> findAll() {
        return partRepository.findAll();
    }

    @Override
    public Part getPartById(int id) {
        return partRepository.findById(id).get();
    }

    @Override
    public void updatePart(Part newPart) {
        partRepository.save(newPart);
    }

    @Override
    public void deletePartById(int id) {
        partRepository.deleteById(id);
    }

    @Override
    public List<Part> getPartsByNameContaining(String filter) {
        return partRepository.findByNameContaining(filter);
    }

    @Override
    public int getRequiredCollect() {
        List<Part> partsRequired = partRepository.findByIsRequiredTrue();
        int min = 0;
        if (partsRequired.size() > 0)
            min = partsRequired.stream().mapToInt(Part::getCount).min().getAsInt();

        return min;
    }

    @Override
    public Page<Part> findPaginated(Pageable pageable, List<Part> partList) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Part> list;

        if(partList.size() < startItem){
            list = Collections.emptyList();
        }else{
            int toIndex = Math.min(startItem + pageSize, partList.size());
            list = partList.subList(startItem, toIndex);
        }

        Page<Part> partPage = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), partList.size());

        return partPage;
    }
}
