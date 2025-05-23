package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Author {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@NotBlank
	private String name;
	@NotBlank
	private String surname;
	@NotBlank
	private LocalDate dayOfBirth;
	private LocalDate dayOfDeath;
	private String nationality;
	private String photo;
	@ManyToMany (mappedBy = "authors")
	private List<Book> books;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public LocalDate getDayOfBirth() {
		return dayOfBirth;
	}

	public void setDayOfBirth(LocalDate dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}

	public LocalDate getDayOfDeath() {
		return dayOfDeath;
	}

	public void setDayOfDeath(LocalDate dayOfDeath) {
		this.dayOfDeath = dayOfDeath;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
}
