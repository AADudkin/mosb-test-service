package ru.mosb.client.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mosb.client.AbstractIT;
import ru.mosb.client.dto.ClientDto;
import ru.mosb.client.dto.XSource;
import ru.mosb.client.exception.ClientSourceValidationException;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ClientValidatorTest extends AbstractIT {

    @Autowired
    private ClientValidator validator;

    public static Stream<Arguments> validationTestSource() {
        return Stream.of(
                Arguments.of(
                        "xSource = MAIL, has first name and email, should pass",
                        new ClientDto()
                                .setFirstName("firstName")
                                .setEmail("email"),
                        XSource.MAIL,
                        true
                ),
                Arguments.of(
                        "xSource = MAIL, no first name, should fail",
                        new ClientDto()
                                .setEmail("email"),
                        XSource.MAIL,
                        false
                ),
                Arguments.of(
                        "xSource = MAIL, no email, should fail",
                        new ClientDto()
                                .setFirstName("firstName"),
                        XSource.MAIL,
                        false
                ),
                Arguments.of(
                        "xSource = MOBILE, has phone, should pass",
                        new ClientDto()
                                .setPhoneNumber("phoneNum"),
                        XSource.MOBILE,
                        true
                ),
                Arguments.of(
                        " xSource = MOBILE, no phone, should fail",
                        new ClientDto()
                                .setFirstName("firstName"),
                        XSource.MOBILE,
                        false
                ),
                Arguments.of(
                        " xSource = BANK, has all required fields, should pass",
                        new ClientDto()
                                .setBankId("bankId")
                                .setFirstName("firstName")
                                .setLastName("lastName")
                                .setPatronymic("patr")
                                .setDateOfBirth(new Date())
                                .setPassport("passport"),
                        XSource.BANK,
                        true
                ),
                Arguments.of(
                        " xSource = BANK, no bank id, should fail",
                        new ClientDto()
                                .setFirstName("firstName")
                                .setLastName("lastName")
                                .setPatronymic("patr")
                                .setDateOfBirth(new Date())
                                .setPassport("passport"),
                        XSource.BANK,
                        false
                ),
                Arguments.of(
                        " xSource = GOSUSLUGI, has all required fields, should pass",
                        new ClientDto()
                                .setBankId("bankId")
                                .setFirstName("firstName")
                                .setLastName("lastName")
                                .setPatronymic("patr")
                                .setDateOfBirth(new Date())
                                .setPlaceOfBirth("place of birth")
                                .setPassport("passport")
                                .setRegistration("registration")
                                .setPhoneNumber("phoneNum"),
                        XSource.GOSUSLUGI,
                        true
                ),
                Arguments.of(
                        " xSource = GOSUSLUGI, no bank id, should fail",
                        new ClientDto()
                                .setFirstName("firstName")
                                .setLastName("lastName")
                                .setPatronymic("patr")
                                .setDateOfBirth(new Date())
                                .setPlaceOfBirth("place of birth")
                                .setPassport("passport")
                                .setRegistration("registration")
                                .setPhoneNumber("phoneNum"),
                        XSource.GOSUSLUGI,
                        false
                ),
                Arguments.of(
                        " xSource = GOSUSLUGI, no registration, should fail",
                        new ClientDto()
                                .setBankId("bankId")
                                .setFirstName("firstName")
                                .setLastName("lastName")
                                .setPatronymic("patr")
                                .setDateOfBirth(new Date())
                                .setPlaceOfBirth("place of birth")
                                .setPassport("passport")
                                .setPhoneNumber("phoneNum"),
                        XSource.GOSUSLUGI,
                        false
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("validationTestSource")
    public void test(String name, ClientDto dto, XSource xSource, boolean shouldPass) {
        try {
            validator.validate(dto, xSource);
            if (!shouldPass) {
                fail("should fail");
            }
        } catch (ClientSourceValidationException e) {
            if (shouldPass) {
                fail("should validate");
            }
        }
    }

}
