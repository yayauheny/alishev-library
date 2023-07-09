package ru.alishev.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alishev.library.dao.PersonDao;
import ru.alishev.library.entity.Person;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDao personDao;

    @Autowired
    public PeopleController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @GetMapping()
    public String findAll(Model model) {
        model.addAttribute("people", personDao.findAll());
        return "people/all";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        Person person = personDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Person not found"));
        model.addAttribute("person", person);
        model.addAttribute("books", personDao.findBooks(id));
        return "people/show_person";
    }

    @GetMapping("/{id}/edit")
    public String showEditView(@PathVariable Integer id, Model model) {
        model.addAttribute("person", personDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Person not found")));
        return "people/edit_person";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") Person person, @PathVariable Integer id){
         personDao.update(id, person);
        return "redirect:/people";
    }
}
