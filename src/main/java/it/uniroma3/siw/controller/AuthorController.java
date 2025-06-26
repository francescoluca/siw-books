package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.AuthorValidator;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.service.AuthorService;

@Controller
public class AuthorController {

	@Autowired
	private AuthorService authorService;

	@Autowired
	private AuthorValidator authorValidator;

	@GetMapping("/authors")
	public String getAuthors(Model model) {
		model.addAttribute("authors", authorService.findAll());
		return "authors.html";
	}

	@GetMapping("/author/{id}")
	public String getAuthor(@PathVariable("id") Long id, Model model) {
		model.addAttribute("author", authorService.findById(id));
		return "author.html";
	}

	@GetMapping("/admin/formNewAuthor")
	public String formNewAuthor(Model model) {
		model.addAttribute("author", new Author());
		return "admin/formNewAuthor.html";
	}

	@PostMapping("/admin/author")
	public String newAuthor(@ModelAttribute("author") Author author, BindingResult bindingResult, Model model) {
		this.authorValidator.validate(author, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.authorService.save(author);
			model.addAttribute("author", author);
			return "redirect:/author/" + author.getId();
		} else {
			return "admin/formNewAuthor";
		}
	}

	@GetMapping("/admin/manageAuthors")
	public String manageAuthors(Model model) {
		model.addAttribute("authors", this.authorService.findAll());
		return "/admin/manageAuthors";
	}

	@GetMapping("/admin/formUpdateAuthor/{id}")
	public String formUpdateAuthor(@PathVariable("id") Long id, Model model) {
		Author author = this.authorService.findById(id);
		model.addAttribute("birthDateString", author.getDayOfBirth() != null ? author.getDayOfBirth().toString() : "");
		model.addAttribute("deathDateString", author.getDayOfDeath() != null ? author.getDayOfDeath().toString() : "");
		model.addAttribute("author", author);
		return "/admin/formUpdateAuthor";
	}

	@PostMapping("/admin/updateAuthor/{id}")
	public String updateAuthor(@PathVariable("id") Long id, Model model,
			@ModelAttribute("author") Author updatedAuthor) {
		Author author = authorService.findById(id);
		author.setName(updatedAuthor.getName());
		author.setSurname(updatedAuthor.getSurname());
		author.setDayOfBirth(updatedAuthor.getDayOfBirth());
		author.setDayOfDeath(updatedAuthor.getDayOfDeath());
		author.setNationality(updatedAuthor.getNationality());
		author.setPhoto(updatedAuthor.getPhoto());
		authorService.save(author);
		return "redirect:/author/" + id;
	}

	@GetMapping("/admin/deleteAuthor/{authorId}")
	public String deleteBook(@PathVariable("authorId") Long authorId, Model model) {
		Author author = this.authorService.findById(authorId);
		this.authorService.delete(author);
		model.addAttribute("authors", this.authorService.findAll());
		return "admin/manageAuthors";
	}
}
