package ru.ryazanov.parts.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.ryazanov.parts.dao.PartRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PartControllerAddTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartRepository partRepository;

    @Test
    public void addPartToParts() throws Exception{

        int count = (int) partRepository.count();

        MockHttpServletRequestBuilder multipart = multipart("/part/create")
                .param("name", "Web-камера")
                .param("count", "15")
                .param("required", "false");

        mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/part/parts"));

        Assert.assertEquals(count + 1, partRepository.count());

        mockMvc.perform(get("/part/parts?filter=Web-камера"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='table-part']//text()[. = 'Web-камера']").exists())
                .andExpect(xpath("//*[@id='table-part']//text()[. = '15']").exists())
                .andExpect(xpath("//*[@id='table-part']//text()[. = 'Нет']").exists());
    }
}
