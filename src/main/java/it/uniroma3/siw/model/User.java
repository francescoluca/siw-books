package it.uniroma3.siw.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@NotBlank
	private String name;
	@NotBlank
	private String surname;
	@NotBlank
	private String email;
	@OneToOne
	private Credentials credentials;
	@OneToMany (mappedBy = "writer")
	private List<Review> reviews;
	@OneToMany
	private List<Book> read;
	@OneToMany
	private List<Book> wantToRead;
	@OneToMany
	private List<Book> currentlyReading;
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
	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	public List<Review> getReviews() {
		return reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Book> getRead() {
		return read;
	}
	public void setRead(List<Book> read) {
		this.read = read;
	}
	public List<Book> getWantToRead() {
		return wantToRead;
	}
	public void setWantToRead(List<Book> wantToRead) {
		this.wantToRead = wantToRead;
	}
	public List<Book> getCurrentlyReading() {
		return currentlyReading;
	}
	public void setCurrentlyReading(List<Book> currentlyReading) {
		this.currentlyReading = currentlyReading;
	}
}
