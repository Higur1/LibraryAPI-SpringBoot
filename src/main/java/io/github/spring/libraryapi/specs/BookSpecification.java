package io.github.spring.libraryapi.specs;

import io.github.spring.libraryapi.model.Book;
import io.github.spring.libraryapi.model.Genre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> isbnEqual(String isbn) {
        return ((root, query, cb)
                -> cb.equal(root.get("isbn"), isbn));
    }

    public static Specification<Book> titleLike(String title) {
        return (root, query, cb)
                -> cb.like(cb.upper(root.get("title")), "%" + title.toUpperCase() + "%");
    }

    public static Specification<Book> genreEqual(Genre genre) {
        return (root, query, cb)
                -> cb.equal(root.get("genre"), genre);
    }

    public static Specification<Book> releaseDateEqual(Integer releaseDate) {
        //SELECT to_char(releaseDate, "YYYY") FROM book
        return (root, query, cb)
                -> cb
                .equal(cb
                                .function(
                                        "to_char",
                                        String.class,
                                        root.get("releaseDate"),
                                        cb
                                                .literal("YYYY")),
                        releaseDate.toString());
    }

    public static Specification<Book> authorNameLike(String name) {
        //join
        return (root, query, cb) -> {
            //manual join
            Join<Object, Object> authorJoin = root.join("author", JoinType.LEFT);
            return cb.like(cb.upper(authorJoin.get("name")), "%" + name.toUpperCase() + "%");
            // return cb.like(cb.upper(root.get("author").get("name")), "%"+name.toUpperCase()+"%"); //automatic join
        };
    }
}
