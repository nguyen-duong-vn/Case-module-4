package com.codegym.laptopshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        }),
        @UniqueConstraint(columnNames = {
                "phone"
        })
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Role role;

    @Column(name = "fullname")
    @NotBlank
    @Size(min = 5, max = 50)
    private String fullName;

    @Column(name = "username")
    @NotBlank
    @Size(min = 5, max = 50)
    private String username;

    @Column(name = "password")
    @NotBlank
    private String password;

    @Column(name = "email")
    @NaturalId
    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    @NotBlank
    @Pattern(regexp = "^0\\d{9}$", message = "10 số, bắt đầu bằng 0")
    private String phone;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "activated")
    private Boolean activated;
}
