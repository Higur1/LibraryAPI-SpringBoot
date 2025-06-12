package io.github.spring.libraryapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;



@Table(name = "book")
@Getter
@Setter
@ToString(exclude = "author")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", nullable = false, length = 20)
    private String isbn;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre", nullable = false)
    private Genre genre;

    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;

    @CreatedDate
    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "user_id")
    private UUID user_id;

    //constraint
    @ManyToOne(
            fetch = FetchType.LAZY//default FetchType.EAGER
    )
    //(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private Author author;
}
