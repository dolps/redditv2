package com.dolplads.model;

import com.dolplads.annotations.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by dolplads on 26/09/16.
 */
@Entity
@NoArgsConstructor
@Data
@ToString
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    private String userName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @Temporal(TemporalType.DATE)
    @Past
    private Date dateOfBirth;

    @Embedded
    private Address address;

    public User(String userName, String email, String password, Date dateOfBirth, Address address) {
        setUserName(userName);
        setEmail(email);
        setPassword(password);
        setDateOfBirth(dateOfBirth);
        setAddress(address);
    }
}
