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
public class PartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private PartService partService;

    @Test
    public void partsCountTest() throws Exception {
        int count;

        int currentPage = 1;
        int pageSize = 10;
        Page<Part> partPage = partService.findPaginated(PageRequest.of(currentPage - 1, pageSize), partService.findAll());
        count = partPage.getContent().size();
        int countPages = partPage.getTotalPages();

        mockMvc.perform(get("/part/parts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='table-part']/tbody/tr").nodeCount(count))
                .andExpect(xpath("//*[@class='page-item active']/a").string("1"))
                .andExpect(xpath("//*[@class='pagination']/li").nodeCount(countPages));


    }

    @Test
    public void partsCountAnotherPage() throws Exception {
        int count;

        int currentPage = 2;
        int pageSize = 10;
        List<Part> allParts = partService.findAll();
        Page<Part> partPage = partService.findPaginated(PageRequest.of(currentPage - 1, pageSize), allParts);

        count = partPage.getContent().size();
        int countPages = partPage.getTotalPages();

        if (allParts.size() > (pageSize * (currentPage - 1))) {
            mockMvc.perform(get("/part/parts?size=" + pageSize + "&page=" + currentPage))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(xpath("//*[@id='table-part']/tbody/tr").nodeCount(count))
                    .andExpect(xpath("//*[@class='page-item active']/a").string(String.valueOf(currentPage)))
                    .andExpect(xpath("//*[@class='pagination']/li").nodeCount(countPages));
        }else{
            mockMvc.perform(get("/part/parts?size=" + pageSize + "&page=" + currentPage))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(xpath("//*[@id='table-part']/tbody/tr").nodeCount(0))
                    .andExpect(xpath("//*[@class='page-item active']/a").doesNotExist())
                    .andExpect(xpath("//*[@class='pagination']/li").nodeCount(countPages));
        }
    }

    @Test
    public void partsCountAnotherPage2() throws Exception {
        int count;

        int currentPage = 4;
        int pageSize = 10;
        List<Part> allParts = partService.findAll();
        Page<Part> partPage = partService.findPaginated(PageRequest.of(currentPage - 1, pageSize), allParts);

        count = partPage.getContent().size();
        int countPages = partPage.getTotalPages();

        if (allParts.size() > (pageSize * (currentPage - 1))) {
            mockMvc.perform(get("/part/parts?size=" + pageSize + "&page=" + currentPage))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(xpath("//*[@id='table-part']/tbody/tr").nodeCount(count))
                    .andExpect(xpath("//*[@class='page-item active']/a").string(String.valueOf(currentPage)))
                    .andExpect(xpath("//*[@class='pagination']/li").nodeCount(countPages));
        }else{
            mockMvc.perform(get("/part/parts?size=" + pageSize + "&page=" + currentPage))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(xpath("//*[@id='table-part']/tbody/tr").nodeCount(0))
                    .andExpect(xpath("//*[@class='page-item active']/a").doesNotExist())
                    .andExpect(xpath("//*[@class='pagination']/li").nodeCount(countPages));
        }
    }
}
