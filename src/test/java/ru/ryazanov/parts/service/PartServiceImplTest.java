package ru.ryazanov.parts.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ryazanov.parts.dao.PartRepository;
import ru.ryazanov.parts.model.Part;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PartServiceImplTest {

    @Autowired
    private PartService partService;

    @MockBean
    private PartRepository partRepository;

    @Test
    public void save() {
        Part part = new Part();
        partService.save(part);
        Mockito.verify(partRepository, Mockito.times(1)).save(part);
    }

    @Test
    public void findAll() {
        List<Part> parts = new ArrayList<>();
        parts.add(new Part());
        parts.add(new Part());

        Mockito.doReturn(parts)
                .when(partRepository)
                .findAll();

        List<Part> resultParts = partService.findAll();

        Assert.assertEquals(resultParts.size(), parts.size());
        Assert.assertTrue(parts.containsAll(resultParts));
        Mockito.verify(partRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getPartById() {
        Part part = new Part();
        part.setId(100);
        part.setName("Pepega");

        Mockito.doReturn(Optional.of(part))
                .when(partRepository)
                .findById(100);

        Part resultPart = partService.getPartById(100);

        Assert.assertEquals(resultPart.getId(), 100);
        Assert.assertEquals(resultPart.getName(), "Pepega");
        Mockito.verify(partRepository, Mockito.times(1)).findById(100);
    }

    @Test
    public void updatePart() {
        Part part = new Part();
        part.setName("New value");
        partService.updatePart(part);
        Mockito.verify(partRepository, Mockito.times(1)).save(part);
    }

    @Test
    public void deletePartById() {
        partService.deletePartById(10);
        Mockito.verify(partRepository, Mockito.times(1)).deleteById(10);
    }

    @Test
    public void getPartsByNameContaining(){
        List<Part> parts = new ArrayList<>();
        Part part = new Part();
        part.setId(100);
        part.setName("Супер компьютерная деталь");

        parts.add(part);

        Mockito.doReturn(parts)
                .when(partRepository)
                .findByNameContaining("Супер компьютерная деталь");

        List<Part> listParts = partService.getPartsByNameContaining("Супер компьютерная деталь");

        Assert.assertEquals(listParts.size(), parts.size());
        Assert.assertTrue(parts.containsAll(listParts));
        Mockito.verify(partRepository, Mockito.times(1)).findByNameContaining("Супер компьютерная деталь");
    }

    @Test
    public void getRequiredCollect() {
        List<Part> parts = new ArrayList<>();

        Part partOne = new Part();
        partOne.setId(1);
        partOne.setName("Мат. плата");
        partOne.setCount(5);
        partOne.setRequired(true);

        Part partTwo = new Part();
        partTwo.setId(2);
        partTwo.setName("Видеокарта");
        partTwo.setCount(3);
        partTwo.setRequired(true);

        parts.add(partOne);
        parts.add(partTwo);

        Mockito.doReturn(parts)
                .when(partRepository)
                .findByIsRequiredTrue();

        int count = partService.getRequiredCollect();

        Assert.assertEquals(count, 3);
        Mockito.verify(partRepository, Mockito.times(1)).findByIsRequiredTrue();
    }

    @Test
    public void getRequiredCollectIfNotExist() {
        List<Part> parts = new ArrayList<>();

        Mockito.doReturn(parts)
                .when(partRepository)
                .findByIsRequiredTrue();

        int count = partService.getRequiredCollect();

        Assert.assertEquals(count, 0);
        Mockito.verify(partRepository, Mockito.times(1)).findByIsRequiredTrue();
    }

    @Test
    public void findPaginated() {
        int currentPage = 1;
        int pageSize = 10;

        List<Part> parts = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            parts.add(new Part());
        }

        Page<Part> partPage = partService.findPaginated(PageRequest.of(currentPage - 1, pageSize), parts);

        Assert.assertEquals(partPage.getTotalPages(),3);
        Assert.assertEquals(partPage.getTotalElements(), 25);
    }
}