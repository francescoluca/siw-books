package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.controller.validator.AuthorValidator;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.repository.AuthorRepository;

@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;
	
	@Autowired
	private AuthorValidator authorValidator;

	public Iterable<Author> findAll() {
		return authorRepository.findAll();
	}
	
	public Author findById(Long id) {
		return authorRepository.findById(id).get();
	}
	
	
}
