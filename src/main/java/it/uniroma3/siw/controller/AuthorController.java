package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
	public String listAuthors(@RequestParam(required = false) String keyword,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sortField", defaultValue = "surname") String sortField,
			@RequestParam(value = "sortDir", defaultValue = "asc") String sortDir, Model model) {

		Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
		Page<Author> authorPage;

		if (keyword != null && !keyword.trim().isEmpty()) {
			authorPage = authorService.searchAuthorsByKeyword(keyword, pageable);
			model.addAttribute("keyword", keyword);
		} else {
			authorPage = authorService.findAll(pageable);
		}

		model.addAttribute("authorsCount", authorPage.getTotalElements());
		model.addAttribute("authors", authorPage.getContent());
		model.addAttribute("authorPage", authorPage);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", authorPage.getTotalPages() == 0 ? 1 : authorPage.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
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
	public String newAuthor(@ModelAttribute("author") Author author, BindingResult bindingResult, Model model,
			@RequestParam("photoFile") MultipartFile photoFile) throws IOException {
		this.authorValidator.validate(author, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.authorService.save(author, photoFile);
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
	public String updateAuthor(@PathVariable("id") Long id, Model model, @ModelAttribute("author") Author updatedAuthor,
			@RequestParam("photoFile") MultipartFile photoFile) throws IOException {
		Author author = authorService.findById(id);
		author.setName(updatedAuthor.getName());
		author.setSurname(updatedAuthor.getSurname());
		author.setDayOfBirth(updatedAuthor.getDayOfBirth());
		author.setDayOfDeath(updatedAuthor.getDayOfDeath());
		author.setNationality(updatedAuthor.getNationality());
		author.setPhoto(updatedAuthor.getPhoto());
		authorService.save(author, photoFile);
		return "redirect:/author/" + id;
	}

	@GetMapping("/admin/deleteAuthor/{authorId}")
	public String deleteBook(@PathVariable("authorId") Long authorId, Model model) {
		Author author = this.authorService.findById(authorId);
		this.authorService.delete(author);
		model.addAttribute("authors", this.authorService.findAll());
		return "admin/manageAuthors";
	}

	@GetMapping("/author/{id}/photo")
	public ResponseEntity<byte[]> photo(@PathVariable Long id) {
		byte[] image = authorService.getPhoto(id);
		if (image == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<>(image, headers, HttpStatus.OK);
	}
}
