package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;

public interface BookRepository extends CrudRepository<Book, Long>{

	boolean existsByTitleAndAuthors(String title, List<Author> authors);

}
