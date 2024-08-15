package com.example.testtasknerdysoft.service.impl;

import com.example.testtasknerdysoft.dto.MemberDTO;
import com.example.testtasknerdysoft.entity.Book;
import com.example.testtasknerdysoft.entity.Member;
import com.example.testtasknerdysoft.exception.ApiRequestException;
import com.example.testtasknerdysoft.exception.BusinessException;
import com.example.testtasknerdysoft.mapper.MemberMapper;
import com.example.testtasknerdysoft.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberMapper memberMapper;
    @InjectMocks
    private MemberServiceImpl memberService;
    private Member member;
    private Member existingMember;
    private MemberDTO memberDTO;

    @BeforeEach
    public void setUp() {
        member = new Member();
        member.setMemberId(1L);
        member.setName("John Doe");
        member.setBorrowedBooks(Collections.emptySet());

        existingMember = new Member();
        existingMember.setMemberId(1L);
        existingMember.setName("John Doe");

        memberDTO = new MemberDTO();
        memberDTO.setName("John Doe");
    }

    @Test
    public void testGetAllMembers_Success() {
        List<Member> memberList = List.of(member);
        List<MemberDTO> memberDTOList = List.of(memberDTO);

        when(memberRepository.findAll()).thenReturn(memberList);
        when(memberMapper.ListToMemberList(memberList)).thenReturn(memberDTOList);

        List<MemberDTO> allMembers = memberService.getAllMembers();

        assertNotNull(allMembers);
        assertEquals(1, allMembers.size());
        assertEquals("John Doe", allMembers.get(0).getName());

        verify(memberRepository).findAll();
        verify(memberMapper).ListToMemberList(memberList);
    }

    @Test
    public void testGetAllMembers_EmptyList() {
        when(memberRepository.findAll()).thenReturn(Collections.emptyList());
        when(memberMapper.ListToMemberList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<MemberDTO> allMembers = memberService.getAllMembers();

        assertNotNull(allMembers);
        assertTrue(allMembers.isEmpty());

        verify(memberRepository).findAll();
        verify(memberMapper).ListToMemberList(Collections.emptyList());
    }

    @Test
    public void testGetMemberById_Success() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberMapper.memberToDTO(member)).thenReturn(memberDTO);

        MemberDTO foundMember = memberService.getMemberById(1L);

        assertNotNull(foundMember);
        assertEquals("John Doe", foundMember.getName());
        verify(memberRepository).findById(1L);
        verify(memberMapper).memberToDTO(member);
    }

    @Test
    public void testGetMemberById_MemberNotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            memberService.getMemberById(1L);
        });

        assertEquals("Member not found with id: 1", thrown.getMessage());
        verify(memberRepository).findById(1L);
        verify(memberMapper, never()).memberToDTO(any(Member.class));
    }

    @Test
    public void testCreateMember_Success() {
        when(memberMapper.dtoToMember(memberDTO)).thenReturn(member);
        when(memberRepository.save(member)).thenReturn(member);
        when(memberMapper.memberToDTO(member)).thenReturn(memberDTO);

        MemberDTO createdMember = memberService.createMember(memberDTO);

        assertNotNull(createdMember);
        assertEquals("John Doe", createdMember.getName());
        verify(memberMapper).dtoToMember(memberDTO);
        verify(memberRepository).save(member);
        verify(memberMapper).memberToDTO(member);
    }

    @Test
    public void testUpdateMember_Success() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(existingMember));
        when(memberRepository.save(existingMember)).thenReturn(existingMember);
        when(memberMapper.memberToDTO(existingMember)).thenReturn(memberDTO);

        MemberDTO updatedMember = memberService.updateMember(1L, memberDTO);

        assertNotNull(updatedMember);
        assertEquals("John Doe", updatedMember.getName());
        verify(memberRepository).findById(1L);
        verify(memberRepository).save(existingMember);
        verify(memberMapper).memberToDTO(existingMember);
    }

    @Test
    public void testUpdateMember_MemberNotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            memberService.updateMember(1L, memberDTO);
        });

        assertEquals("Member not found with id: 1", thrown.getMessage());
        verify(memberRepository, never()).save(any(Member.class));
        verify(memberMapper, never()).memberToDTO(any(Member.class));
    }

    @Test
    public void testDeleteMember_HasBorrowedBooks() {
        member.setBorrowedBooks(Set.of(new Book()));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            memberService.deleteMember(1L);
        });

        assertEquals("Cannot delete a member with borrowed books", thrown.getMessage());
        verify(memberRepository, never()).deleteById(1L);
    }

    @Test
    public void testDeleteMember_MemberNotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            memberService.deleteMember(1L);
        });

        assertEquals("Member not found with id: 1", thrown.getMessage());
        verify(memberRepository, never()).deleteById(1L);
    }

    @Test
    public void testDeleteMember_Success() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        memberService.deleteMember(1L);

        verify(memberRepository).deleteById(1L);
    }
}