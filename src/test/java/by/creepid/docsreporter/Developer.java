/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter;

import java.util.Date;

/**
 *
 * @author mirash
 */
public class Developer {

    private String name;
    private String lastName;
    private String mail;
    private Date birthDate;

    public Developer(String name, String lastName, String mail, Date birthDate) {
        this.name = name;
        this.lastName = lastName;
        this.mail = mail;
        this.birthDate = birthDate;
    }

    public Developer(String name, String lastName, String mail) {
        this.name = name;
        this.lastName = lastName;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
