package com.example.testtasknerdysoft.service;

import com.example.testtasknerdysoft.dto.MemberDTO;

import java.util.List;

public interface MemberService {

    List<MemberDTO> getAllMembers();

    MemberDTO getMemberById(Long id);

    MemberDTO createMember(MemberDTO member);

    MemberDTO updateMember(Long id, MemberDTO member);

    void deleteMember(Long id);

}
