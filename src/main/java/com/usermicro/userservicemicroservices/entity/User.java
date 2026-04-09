package com.usermicro.userservicemicroservices.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "ID")
    private String userId;

    @Column(name = "name")
    private String name;

    @Column(name= "email")
    private String email;

    @Column(name="about")
    private String about;
}
