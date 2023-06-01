package ru.mosb.client.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ClientDto {

    private Long id;
    private String bankId;
    private String firstName;
    private String lastName;
    private String patronymic;
    private Date dateOfBirth;
    private String placeOfBirth;
    @Pattern(regexp = "^\\d{4} \\d{6}$", message = "passport should have XXXX XXXXXX format")
    private String passport;
    @Pattern(regexp = "^7\\d{10}$", message = "phone should have 7XXXXXXXXXX format")
    private String phoneNumber;
    @Email
    private String email;
    private String registration;
    private String actualLivingAddress;

}
