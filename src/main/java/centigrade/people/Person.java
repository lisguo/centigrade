package centigrade.people;

import javax.persistence.*;

@Entity
@Table(name = "castmembers")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String biography;
    private String castType;

    public Person() {
    }

    public Person(long id, String firstname, String biography) {
        this.id = id;
        this.firstName = firstname;
        this.biography = biography;
    }

    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "firstname")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "middlename")
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Column(name = "lastname")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "biography")
    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) { this.biography = biography; }

    @Column(name = "casttype")
    public String getCastType() { return castType; }

    public void setCastType(String castType) { this.castType = castType; }

    @Override
    public String toString() {
        String fullname = firstName;

        if (middleName != null) {
            fullname += " " + middleName;
        }
        if (lastName != null) {
            fullname += " " + lastName;
        }
        return fullname;
    }
}

