package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.BookValidator;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;

@Controller
public class BookController {

	@Autowired 
	private BookService bookService;
	
	@Autowired
	private AuthorService authorService;
	
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
		model.addAttribute("allAuthors",this.authorService.findAll());
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
	
	@GetMapping("/admin/formUpdateBook/{id}")
	public String formUpdateBook(@PathVariable("id") Long id,Model model) {
		model.addAttribute("book",this.bookService.findById(id));
		return "/admin/formUpdateBook";
	}
	
	@GetMapping("/admin/updateAuthors/{id}")
	public String updateAuthors(@PathVariable("id") Long bookId, Model model) {

		List<Author> authorsToAdd = this.authorsToAdd(bookId);
		model.addAttribute("authorsToAdd", authorsToAdd);
		model.addAttribute("book", this.bookService.findById(bookId));

		return "admin/authorsToAdd.html";
	}
	
	@GetMapping("/admin/addAuthorToBook/{authorId}/{bookId}")
	public String addAuthorToBook(@PathVariable("authorId") Long authorId,@PathVariable("bookId") Long bookId,Model model) {
		Book book = this.bookService.findById(bookId);
		Author author = this.authorService.findById(authorId);
		List<Author> authors = book.getAuthors();
		authors.add(author);
		this.bookService.save(book);
		List<Author> authorsToAdd = authorsToAdd(bookId);
		model.addAttribute("book", book);
		model.addAttribute("authorsToAdd",authorsToAdd);
		return "admin/authorsToAdd.html";
	}
	
	@GetMapping("/admin/removeAuthorFromBook/{authorId}/{bookId}")
	public String removeAuthorFromBook(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId, Model model) {
		Book book = this.bookService.findById(bookId);
		Author author = this.authorService.findById(authorId);
		List<Author> authors = book.getAuthors();
		authors.remove(author);
		this.bookService.save(book);
		List<Author> authorsToAdd = authorsToAdd(bookId);
		model.addAttribute("book", book);
		model.addAttribute("authorsToAdd",authorsToAdd);
		return "admin/authorsToAdd.html";
	}
	
	private List<Author> authorsToAdd(Long bookId) {
		List<Author> authorsToAdd = new ArrayList<>();

		for (Author a : authorService.findAuthorsNotInBook(bookId)) {
			authorsToAdd.add(a);
		}
		return authorsToAdd;
	}
}
