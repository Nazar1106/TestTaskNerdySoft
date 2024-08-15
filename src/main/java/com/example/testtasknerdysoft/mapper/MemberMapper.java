package com.example.testtasknerdysoft.mapper;

import com.example.testtasknerdysoft.dto.MemberDTO;
import com.example.testtasknerdysoft.entity.Member;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    Member dtoToMember(MemberDTO memberDTO);

    MemberDTO memberToDTO(Member member);

    List<MemberDTO> ListToMemberList(List<Member> memberList);
}
