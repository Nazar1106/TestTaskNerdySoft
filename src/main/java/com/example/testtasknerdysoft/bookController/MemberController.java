package com.example.testtasknerdysoft.bookController;

import com.example.testtasknerdysoft.dto.MemberDTO;
import com.example.testtasknerdysoft.exception.ApiRequestException;
import com.example.testtasknerdysoft.exception.BusinessException;
import com.example.testtasknerdysoft.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "Retrieve all members", description = "Retrieves a list of all members in the system. This endpoint provides details for every member available in the database")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<MemberDTO> getAllMembers() throws ApiRequestException {
        return memberService.getAllMembers();
    }

    @Operation(summary = "Retrieve a member by ID", description = "Retrieves detailed information about a specific member by their ID")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public MemberDTO getMemberById(@PathVariable Long id) throws BusinessException {

        return memberService.getMemberById(id);
    }

    @Operation(summary = "Create a new member", description = "Creates a new member in the system. The request body must contain the details of the member to be created")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MemberDTO createMember(@RequestBody @Valid MemberDTO member) throws ApiRequestException, BusinessException {

        return memberService.createMember(member);
    }


    @Operation(summary = "Update an existing member", description = "Updates the details of an existing member identified by the ID. The request body must contain the updated details of the member")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public MemberDTO updateMember(@PathVariable Long id, @RequestBody @Valid MemberDTO member) throws BusinessException, ApiRequestException {
        return memberService.updateMember(id, member);
    }

    @Operation(summary = "Delete a member by ID", description = "Deletes an existing member identified by the given ID. This operation will remove the member from the system")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Long id) throws ApiRequestException, BusinessException {
        memberService.deleteMember(id);
    }
}




