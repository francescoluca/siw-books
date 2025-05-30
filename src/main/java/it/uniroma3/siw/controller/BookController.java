package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.BookValidator;
import it.uniroma3.siw.model.Book;
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
	@GetMapping("/admin/formNewBook")
	public String formNewBook(Model model){
		model.addAttribute("book",new Book());
		return "admin/formNewBook.html";
	}
	@PostMapping("/admin/book")
	public String newBook(@ModelAttribute("book") Book book,BindingResult bindingResult,Model model) {
		this.bookValidator.validate(book, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.bookService.save(book);
			model.addAttribute("book",book);
			return "redirect:/book/"+book.getId();
		}else {
			return "admin/formNewBook";
		}
	}
	@GetMapping("/admin/manageBooks")
	public String manageBooks(Model model) {
		model.addAttribute("books",this.bookService.findAll());
		return "/admin/manageBooks";
	}
}
