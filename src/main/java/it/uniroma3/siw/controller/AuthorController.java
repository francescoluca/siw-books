package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.controller.validator.AuthorValidator;
import it.uniroma3.siw.service.AuthorService;

@Controller
public class AuthorController {

	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private AuthorValidator authorValidator;
	
	@GetMapping("/authors")
	public String getAuthors(Model model) {
		model.addAttribute("authors",authorService.findAll());
		return "authors.html";
	}
	
	@GetMapping("/author/{id}")
	public String getAuthor(@PathVariable ("id") Long id,Model model) {
		model.addAttribute("author",authorService.findById(id));
		return "author.html";
	}
		
}

