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

    //JPQL -> references to entity's - use properties of entity's
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
    List<Book> findByGenre(@Param("genre") Genre genre, @Param("price") BigDecimal price); //maybe need to convert BigDecimal TO

    @Query("SELECT book from Book book WHERE book.genre = ?1 order by ?2")
    List<Book> findByGenrePositionalParam(Genre genre, BigDecimal price); //maybe need to convert BigDecimal TO

    //need to write operation
    @Modifying
    @Transactional
    @Query("DELETE from Book WHERE genre = ?1")
    void deleteByGenre(Genre genre);

    @Modifying
    @Transactional
    @Query("UPDATE Book SET releaseDate = ?1") //need where
    void updateReleaseDate(LocalDate newReleaseDate);

    boolean existsByAuthor(Author author);


}
