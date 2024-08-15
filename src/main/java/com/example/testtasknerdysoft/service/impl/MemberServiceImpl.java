package com.example.testtasknerdysoft.service.impl;

import com.example.testtasknerdysoft.dto.MemberDTO;
import com.example.testtasknerdysoft.entity.Member;
import com.example.testtasknerdysoft.exception.ApiRequestException;
import com.example.testtasknerdysoft.exception.BusinessException;
import com.example.testtasknerdysoft.mapper.MemberMapper;
import com.example.testtasknerdysoft.repository.MemberRepository;
import com.example.testtasknerdysoft.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    private final MemberMapper memberMapper;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberDTO> getAllMembers() throws ApiRequestException {

        List<Member> memberList = memberRepository.findAll().stream().toList();

        return memberMapper.ListToMemberList(memberList);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new BusinessException("Member not found with id: " + id));
        return memberMapper.memberToDTO(member);
    }

    @Override
    @Transactional
    public MemberDTO createMember(MemberDTO member) throws ApiRequestException {
        Member newMember = memberMapper.dtoToMember(member);
        memberRepository.save(newMember);
        return memberMapper.memberToDTO(newMember);
    }

    @Override
    @Transactional
    public MemberDTO updateMember(Long id, MemberDTO member) {
        Member existingMember = memberRepository.findById(id).orElseThrow(() -> new BusinessException("Member not found with id: " + id));
        existingMember.setName(member.getName());
        memberRepository.save(existingMember);
        return memberMapper.memberToDTO(existingMember);
    }

    @Override
    @Transactional
    public void deleteMember(Long id) {

        Member member = memberRepository.findById(id).orElseThrow(() -> new BusinessException("Member not found with id: " + id));

        if (member.getBorrowedBooks().isEmpty()) {
            memberRepository.deleteById(id);
        } else {
            throw new ApiRequestException("Cannot delete a member with borrowed books");
        }
    }

}
