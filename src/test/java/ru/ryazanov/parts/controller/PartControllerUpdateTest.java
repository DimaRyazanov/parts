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
import ru.ryazanov.parts.model.Part;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PartControllerUpdateTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartRepository partRepository;

    @Test
    public void updatePartAndCheckPartsAndCountCollect() throws Exception{

        int count = (int) partRepository.count();

        Part oldPart = partRepository.findById(3).get();

        mockMvc.perform(get("/part/edit/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//input[@id='part_id']/@value").string(String.valueOf(oldPart.getId())))
                .andExpect(xpath("//input[@id='part_name']/@value").string(oldPart.getName()))
                .andExpect(xpath("//input[@id='part_count']/@value").string(String.valueOf(oldPart.getCount())))
                .andExpect(xpath("//input[@id='part_required']/@value").string(oldPart.isRequired() ? "true" : "false"));

        MockHttpServletRequestBuilder multipart = multipart("/part/edit/3")
                .param("id", "3")
                .param("name", "Процессор update")
                .param("count", "5")
                .param("required", "true");

        mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/part/parts"));

        Part newPart = partRepository.findById(3).get();

        Assert.assertEquals(newPart.getId(), 3);
        Assert.assertEquals(newPart.getName(), "Процессор update");
        Assert.assertEquals(newPart.getCount(), 5);
        Assert.assertEquals(newPart.isRequired(), true);

        mockMvc.perform(get("/part/parts?filter=" + newPart.getName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='table-part']/tbody/tr/td[@id='3']").exists())
                .andExpect(xpath("//*[@id='table-part']/tbody/tr/td[@id='3_name']").string(newPart.getName()))
                .andExpect(xpath("//*[@id='table-part']/tbody/tr/td[@id='3_count']").string(String.valueOf(newPart.getCount())))
                .andExpect(xpath("//*[@id='table-part']/tbody/tr/td[@id='3_required']/div").string(newPart.isRequired() ? "Да" : "Нет"));

    }
}
