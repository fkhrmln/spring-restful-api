package com.example.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

    @Id
    private String id;

    private String name;

    private String phone;

    private String email;

    @ManyToOne
    @JoinColumn(
            name = "username",
            referencedColumnName = "username"
    )
    private User user;

    @OneToMany(mappedBy = "contact")
    private List<Address> addresses;

}
