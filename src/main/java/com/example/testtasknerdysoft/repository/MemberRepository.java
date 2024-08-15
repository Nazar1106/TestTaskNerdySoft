package com.example.testtasknerdysoft.repository;

import com.example.testtasknerdysoft.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByName(String name);

}
