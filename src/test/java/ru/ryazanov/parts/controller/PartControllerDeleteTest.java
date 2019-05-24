package ru.ryazanov.parts.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.ryazanov.parts.dao.PartRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PartControllerDeleteTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartRepository partRepository;

    @Test
    public void deletePartFromParts() throws Exception{
        int count = (int) partRepository.count();

        mockMvc.perform(get("/part/delete/4"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/part/parts"));

        Assert.assertEquals(count - 1, partRepository.count());
        Assert.assertFalse(partRepository.existsById(4));
    }
}
