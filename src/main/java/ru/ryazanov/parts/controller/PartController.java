package ru.ryazanov.parts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ryazanov.parts.model.Part;
import ru.ryazanov.parts.service.PartService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(path = "/part")
public class PartController {
    @Autowired
    private PartService partService;

    @GetMapping("/create")
    public String createPart(Model model){
        Part part = new Part();
        model.addAttribute("part", part);
        return "createPart";
    }

    @PostMapping("/create")
    public String addPart(@Valid Part part, BindingResult result){
        if (result.hasErrors()){
            return "createPart";
        }

        partService.save(part);
        return "redirect:/part/parts";
    }

    @GetMapping(value = "/parts")
    public String listParts(@RequestParam(required = false, defaultValue = "") String filter,
                            @RequestParam("page") Optional<Integer> page,
                            @RequestParam("size") Optional<Integer> size,
                            Model model){

        List<Part> parts;
        if (filter != null && !filter.isEmpty()){
            parts = partService.getPartsByNameContaining(filter);
            model.addAttribute("filter", filter);
        }else{
            parts = partService.findAll();
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Part> partPage = partService.findPaginated(PageRequest.of(currentPage - 1, pageSize), parts);

        model.addAttribute("partPage", partPage);
        model.addAttribute("canCollect", partService.getRequiredCollect());

        int totalPages = partPage.getTotalPages();
        if (totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "listParts";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        Part part = partService.getPartById(id);
        model.addAttribute("part", part);
        return "editPart";
    }

    @PostMapping(value = "/edit/{id}")
    public String editSave(@PathVariable int id, @Valid Part part, BindingResult result){

        if (result.hasErrors()){
            return "editPart";
        }
        partService.updatePart(part);
        return "redirect:/part/parts";
    }

    @GetMapping(value = "/delete/{id}")
    public String delete(@PathVariable int id){
        partService.deletePartById(id);
        return "redirect:/part/parts";
    }
}
