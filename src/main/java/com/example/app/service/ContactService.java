package com.example.app.service;

import com.example.app.dto.*;
import com.example.app.entity.Contact;
import com.example.app.entity.User;
import com.example.app.repository.ContactRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public ContactResponse createContact(User user, CreateContactRequest request) {
        validationService.validate(request);

        Contact contact = Contact.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .user(user)
                .build();

        contactRepository.save(contact);

        return ContactResponse.builder()
                .id(contact.getId())
                .name(contact.getName())
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .build();
    }

    @Transactional(readOnly = true)
    public ContactResponse getContact(User user, String contactId) {
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));

        return ContactResponse.builder()
                .id(contact.getId())
                .name(contact.getName())
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .build();
    }

    @Transactional
    public ContactResponse updateContact(User user, UpdateContactRequest request) {
        Contact contact = contactRepository.findFirstByUserAndId(user, request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));

        contact.setName(request.getName());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());

        contactRepository.save(contact);

        return ContactResponse.builder()
                .id(contact.getId())
                .name(contact.getName())
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .build();
    }

    @Transactional
    public void removeContact(User user, String contactId) {
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));

        contactRepository.delete(contact);
    }

    @Transactional(readOnly = true)
    public SearchContactResponse searchContact(User user, SearchContactRequest request) {
        Specification<Contact> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            predicates.add(criteriaBuilder.equal(root.get("user"), user));

            if (request.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), STR."%\{request.getName()}%"));
            }

            if (request.getPhone() != null) {
                predicates.add(criteriaBuilder.like(root.get("phone"), STR."%\{request.getPhone()}%"));
            }

            if (request.getEmail() != null) {
                predicates.add(criteriaBuilder.like(root.get("email"), STR."%\{request.getEmail()}%"));
            }

            return criteriaQuery.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<Contact> pages = contactRepository.findAll(specification, pageable);

        List<ContactResponse> contactResponses = pages.getContent()
                .stream()
                .map(contact -> ContactResponse.builder()
                        .id(contact.getId())
                        .name(contact.getName())
                        .phone(contact.getPhone())
                        .email(contact.getEmail())
                        .build()
                )
                .toList();

        PageResponse pageResponse = PageResponse.builder()
                .currentPage(pages.getNumber())
                .totalPage(pages.getTotalPages())
                .size(pages.getSize())
                .build();

        return SearchContactResponse.builder()
                .page(pageResponse)
                .contacts(contactResponses)
                .build();
    }

}
