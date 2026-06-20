// Library.java
package com.akshay.phase07_mini_project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Library {
	
	private final String name;
	private final List<Book> books;
	
	public Library(String name) {
		if(name == null || name.isBlank()) {
			throw new IllegalArgumentException("Library name cannot be null or blank");
		}
		this.name = name.trim();
		this.books = new ArrayList<>();
	}
	
	public void addBook(Book book) {
		if(book == null) {
			throw new IllegalArgumentException("Book cannot be null");
		}
		if(books.contains(book)) {
			throw new IllegalStateException("Book with ISBN " + book.getIsbn() + " already exits in the library");
		}
		books.add(book);
	}
	
	public Book borrowBook(String isbn) {
		if(isbn == null || isbn.isBlank()) {
			throw new IllegalArgumentException("ISBN cannot be null or blank");
		}
		
		// returns book if find int library otherwise throw exception [ uses Optional concept ]
		Book book = findByIsbn(isbn).orElseThrow(()-> new IllegalArgumentException("Book with ISBN " + isbn + " not found in library"));
		
		// if find but not available then throw exception
		if(!book.isAvailable()) {
			throw new IllegalStateException("Book '" + book.getTitle() + "' is currently not available");
		}
		
		// found and available , and setting availability to false
		book.setAvailable(false);
		
		return book; // return the book
	}
	
	public void returnBook(String isbn) {
		if(isbn == null || isbn.isBlank()) {
			throw new IllegalArgumentException("ISBN cannot be null or blank");
		}
		
		Book book = findByIsbn(isbn).orElseThrow(() -> new IllegalArgumentException("Book with ISBN " + isbn + " not found in the library"));
		
		// if book was not borrowed then throw exception
		if(book.isAvailable()) {
			throw new IllegalStateException("Book '" + book.getTitle() + "' was not borrowed");
		}
		
		// if book is borrowed and after return set availability to true
		book.setAvailable(true);
	}
	
	public List<Book> searchByTitle(String title){
		if(title == null || title.isBlank()) {
			throw new IllegalArgumentException("Search title cannot be null or blank");
		}
		
		String lowerTitle = title.toLowerCase().trim();
		
		List<Book> result = new ArrayList<>();
		for(Book book : books) {
			if(book.getTitle().toLowerCase().contains(lowerTitle)) {
				result.add(book);
			}
		}
		
		return Collections.unmodifiableList(result);
	}

	public List<Book> searchByAuthor(String author){
		if(author == null || author.isBlank()) {
			throw new IllegalArgumentException("search author cannot be null or blank");
		}
		
		String lowerAuthor = author.toLowerCase().trim();
		
		List<Book> result = new ArrayList<>();
		for(Book book : books) {
			if(book.getAuthor().toLowerCase().contains(lowerAuthor)) {
				result.add(book);
			}
		}
		
		return Collections.unmodifiableList(result);
	}
	
	
    // Returns count of available books.
	public long getAvailableBookCount() {
		return books.stream()
				.filter(Book::isAvailable)
				.count();
	}
	
	// Returns total book count.
	public int getTotalBookCount() {
		return books.size();
	}
	
	public String getName() {
		return name;
	}
	
	Optional<Book> findByIsbn(String isbn) {
        return books.stream()
                    .filter(b -> b.getIsbn().equals(isbn))
                    .findFirst();
    }
}
