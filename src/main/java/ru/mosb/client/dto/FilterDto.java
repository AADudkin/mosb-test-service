package ru.mosb.client.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class FilterDto {

    private String firstName;
    private String lastName;
    private String patronymic;
    private String phoneNumber;
    private String email;
}
