package com.example.app.controller;

import com.example.app.dto.*;
import com.example.app.entity.User;
import com.example.app.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping(
            path = "/api/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<ContactResponse>> createContact(User user, @RequestBody CreateContactRequest request) {
        ContactResponse contactResponse = contactService.createContact(user, request);

        Response<ContactResponse> response = Response.<ContactResponse>builder()
                .status("OK")
                .message("Contact Created Successfully")
                .data(contactResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(
            path = "/api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<ContactResponse>> getContact(User user, @PathVariable(name = "contactId") String contactId) {
        ContactResponse contactResponse = contactService.getContact(user, contactId);

        Response<ContactResponse> response = Response.<ContactResponse>builder()
                .status("OK")
                .data(contactResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(
            path = "/api/contacts/{contactId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<ContactResponse>> updateContact(
            User user,
            @RequestBody UpdateContactRequest request,
            @PathVariable(name = "contactId") String contactId
    ) {
        request.setId(contactId);

        ContactResponse contactResponse = contactService.updateContact(user, request);

        Response<ContactResponse> response = Response.<ContactResponse>builder()
                .status("OK")
                .message("Contact Updated Successfully")
                .data(contactResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(
            path = "/api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<?>> removeContact(User user, @PathVariable(name = "contactId") String contactId) {
        contactService.removeContact(user, contactId);

        Response<Object> response = Response.builder()
                .status("OK")
                .message("Contact Deleted Successfully")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/api/contacts",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<SearchContactResponse>> searchContact(
            User user,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        SearchContactRequest request = SearchContactRequest.builder()
                .name(name)
                .phone(phone)
                .email(email)
                .page(page)
                .size(size)
                .build();

        SearchContactResponse searchContactResponse = contactService.searchContact(user, request);

        Response<SearchContactResponse> response = Response.<SearchContactResponse>builder()
                .status("OK")
                .data(searchContactResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
