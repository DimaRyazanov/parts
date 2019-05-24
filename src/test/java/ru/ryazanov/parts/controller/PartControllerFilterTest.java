package ru.ryazanov.parts.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.ryazanov.parts.dao.PartRepository;
import ru.ryazanov.parts.model.Part;
import ru.ryazanov.parts.service.PartService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PartControllerFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private PartService partService;

    @Test
    public void partsCountTestByFilter() throws Exception {
        int count;
        String filter = "Видеокарта";

        int currentPage = 1;
        int pageSize = 10;
        List<Part> filterParts = partService.getPartsByNameContaining(filter);

        Page<Part> partPage = partService.findPaginated(PageRequest.of(currentPage - 1, pageSize), filterParts);
        count = partPage.getContent().size();
        int countPages = partPage.getTotalPages();

        if (countPages > 0) {
            mockMvc.perform(get("/part/parts?filter=" + filter))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(xpath("//*[@id='table-part']/tbody/tr").nodeCount(count))
                    .andExpect(xpath("//*[@class='page-item active']/a").string(String.valueOf(currentPage)))
                    .andExpect(xpath("//*[@class='pagination']/li").nodeCount(countPages));
        } else {
            mockMvc.perform(get("/part/parts?filter=" + filter))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(xpath("//*[@id='table-part']/tbody/tr").nodeCount(0))
                    .andExpect(xpath("//*[@class='page-item active']/a").doesNotExist())
                    .andExpect(xpath("//*[@class='pagination']/li").nodeCount(countPages));
        }

    }

    @Test
    public void partsCountTestByBadFilter() throws Exception {
        int count;
        String filter = "BAD NAME 1234";

        int currentPage = 1;
        int pageSize = 10;
        List<Part> filterParts = partService.getPartsByNameContaining(filter);

        Page<Part> partPage = partService.findPaginated(PageRequest.of(currentPage - 1, pageSize), filterParts);
        count = partPage.getContent().size();
        int countPages = partPage.getTotalPages();

        if (countPages > 0) {
            mockMvc.perform(get("/part/parts?filter=" + filter))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(xpath("//*[@id='table-part']/tbody/tr").nodeCount(count))
                    .andExpect(xpath("//*[@class='page-item active']/a").string(String.valueOf(currentPage)))
                    .andExpect(xpath("//*[@class='pagination']/li").nodeCount(countPages));
        } else {
            mockMvc.perform(get("/part/parts?filter=" + filter))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(xpath("//*[@id='table-part']/tbody/tr").nodeCount(0))
                    .andExpect(xpath("//*[@class='page-item active']/a").doesNotExist())
                    .andExpect(xpath("//*[@class='pagination']/li").nodeCount(countPages));
        }

    }
}
