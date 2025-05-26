package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.controller.validator.BookValidator;
import it.uniroma3.siw.service.BookService;

@Controller
public class BookController {

	@Autowired 
	private BookService bookService;
	
	@Autowired
	private BookValidator bookValidator;
	
	@GetMapping("/book/{id}")
	public String getBook(@PathVariable("id") Long id, Model model) {
		model.addAttribute("book", this.bookService.findById(id));
		return "book.html";
	}

	@GetMapping("/books")
	public String getBooks(Model model) {		
		model.addAttribute("books", this.bookService.findAll());
		return "books.html";
	}
}
