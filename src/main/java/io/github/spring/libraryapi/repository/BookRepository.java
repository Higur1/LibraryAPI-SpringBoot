package io.github.spring.libraryapi.repository;

import io.github.spring.libraryapi.model.Author;
import io.github.spring.libraryapi.model.Book;
import io.github.spring.libraryapi.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//DOC - JPA Query Methods - docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

    List<Book> findByAuthor(Author author);

    List<Book> findByTitle(String title);

    Optional<Book> findByIsbn(String isbn);

    @Query("SELECT book from Book as book ORDER BY book.title, book.price")
    List<Book> listAllOrderByTitleAndPrice();

    @Query("SELECT author FROM Book book join book.author author")
    List<Author> listBookAuthors();

    @Query("SELECT DISTINCT book.title from Book book")
    List<String> listOfDistinctName();

    @Query("""
           SELECT book.genre
           FROM Book book
           JOIN book.author author
           WHERE author.nationality = 'Brazilian'
           ORDER BY book.genre
            """)
    List<String> listOfGenreBrazilianAuthors();

    @Query("SELECT book from Book book WHERE book.genre = :genre order by :price")
    List<Book> findByGenreOrderPrice(@Param("genre") Genre genre, @Param("price") BigDecimal price);

    @Modifying
    @Transactional
    @Query("DELETE FROM Book book WHERE book.genre = ?1")
    void deleteByGenre(Genre genre);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Book book SET book.releaseDate = ?1 WHERE book.id = ?2")
    void updateReleaseDate(LocalDate newReleaseDate, UUID uuid);

    boolean existsByAuthor(Author author);
}
