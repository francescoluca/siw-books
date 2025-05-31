package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private BookService bookService;
	
	@GetMapping("/profile")
	public String profile(Model model) {
		
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		model.addAttribute("user",userService.getUser(credentials.getId()));
		return "/profile";
	}
	@GetMapping("/readBook/{bookId}")
	public String readBook(@PathVariable ("bookId") Long bookId,Model model){
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	Book book = bookService.findById(bookId);
    	User currentUser = userService.getUser(credentials.getId());
    	currentUser.getReadBooks().add(book);
    	userService.saveUser(currentUser);
    	model.addAttribute("user",currentUser);
    	model.addAttribute("book",book);
    	return "/book";
	}
}
