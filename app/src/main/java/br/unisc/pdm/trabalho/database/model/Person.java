package br.unisc.pdm.trabalho.database.model;

public class Person extends AbstractModel {

    private String name;
    private String email;
    private String registration_number; /* Save it as string, because we can support different registration numbers pattern s */
    private String photo; /* Save photo's name in filesystem as string. Don't storage file in the database. Maybe, may we change storage in the future to a cloud solution? */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registrationNumber) {
        this.registration_number = registrationNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return getName();
    }
}
