package it.uniroma3.siw.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.service.BookService;

@RestController
public class BookRestController {

	@Autowired
	private BookService bookService;

}
