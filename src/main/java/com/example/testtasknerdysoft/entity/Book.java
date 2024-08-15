package com.example.testtasknerdysoft.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String title;

    private String author;

    private int amount;

    @JsonIgnore
    @ManyToMany(mappedBy = "borrowedBooks")
    private Set<Member> borrowers = new HashSet<>();
}
