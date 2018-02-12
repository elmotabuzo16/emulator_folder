package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.User;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.date.LocalDate;

public class PersonalDetailsDTO {
    private final String givenName;
    private final String familyName;
    private final LocalDate dateOfBirth;
    private final String gender;
    private final String initials;
    private final String dateOfBirthString;
    private final int age;

    public PersonalDetailsDTO(String givenName, String familyName, LocalDate dateOfBirth, String gender, String initials, String dateOfBirthString, int age) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.initials = initials;
        this.dateOfBirthString = dateOfBirthString;
        this.age = age;
    }

    public String getDateOfBirthString() {
        return dateOfBirthString;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getInitials() {
        return initials;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public static class Mapper implements DataStore.ModelMapper<User, PersonalDetailsDTO> {
        @Override
        public PersonalDetailsDTO mapModel(User model) {
            if (model == null) {
                return new PersonalDetailsDTO("", "", LocalDate.create(""), "", "", "", 0);
            }
            return new PersonalDetailsDTO(model.getGivenName(), model.getFamilyName(),
                    new LocalDate(model.getDateOfBirth()), mapGender(model.getGender()), getInitial(model.getGivenName()), model.getDateOfBirth(), getAge(new LocalDate(model.getDateOfBirth())));
        }

        private String getInitial(String name) {
            return TextUtilities.isNullOrWhitespace(name) ? "" : name.substring(0, 1);
        }

        private int getAge(LocalDate localBirthDate) {
            return TimeUtilities.getAge(localBirthDate);
        }

        private String mapGender(String genderType) {
            switch (genderType) {
                case "1":
                    return "Male";
                case "2":
                    return "Female";
                default:
                    return "Unknown";
            }
        }

    }
}
