package com.example.testtasknerdysoft.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String name;

    private LocalDate membershipDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "member_books",
            joinColumns = @JoinColumn(name = "memberId"),
            inverseJoinColumns = @JoinColumn(name = "bookId"))
    private Set<Book> borrowedBooks = new HashSet<>();
}
