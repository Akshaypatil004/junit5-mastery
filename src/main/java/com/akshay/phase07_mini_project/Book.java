// Book.java
package com.akshay.phase07_mini_project;


public class Book {
	
	private final String isbn;
	private final String title;
	private final String author;
	private boolean available;
	
	public Book(String isbn, String title,String author) {
		if(isbn == null || isbn.isBlank()) {
			throw new IllegalArgumentException("ISBN cannot be null or blank");
		}
		if(title == null || title.isBlank()) {
			throw new IllegalArgumentException("Title cannot be null or blank");
		}
		if(author == null || author.isBlank()) {
			throw new IllegalArgumentException("Author cannot be null or blank");
		}
		
		this.isbn = isbn.trim();
		this.title = title.trim();
		this.author = author.trim();
		this.available = true; 
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public boolean isAvailable() {
		return available;
	}

    @Override
    public boolean equals(Object o) {
        // Two books are the same if they have the same ISBN
        // This is important for collection operations (contains, remove, etc.)
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }

	@Override
	public String toString() {
		return "Book [isbn=" + isbn + ", title=" + title + ", author=" + author + ", available=" + available + "]";
	}
	
	

}
