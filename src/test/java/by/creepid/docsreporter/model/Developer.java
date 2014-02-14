/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.model;

import by.creepid.docsreporter.context.annotations.NullValue;
import by.creepid.docsreporter.context.annotations.TextStyling;
import fr.opensagres.xdocreport.core.document.SyntaxKind;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mirash
 */
public class Developer {

    @TextStyling(syntaxKind = SyntaxKind.Html)
    private String name;
    private String lastName;
    @NullValue(value = "Enter your mail, please")
    private String mail;
    private Date birthDate;
    private List<Role> roles;
    private WorkingStatus status = WorkingStatus.fullTime;

    public Developer(String name, String lastName, String mail, Date birthDate, List<Role> roles, WorkingStatus status) {
        this(name, lastName, mail, birthDate);
        this.roles = roles;
        this.status = status;
    }

    public Developer(String name, String lastName, String mail, Date birthDate, List<Role> roles) {
        this(name, lastName, mail, birthDate);
        this.roles = roles;
    }

    public Developer(String name, String lastName, String mail, Date birthDate) {
        this(name, lastName, mail);
        this.birthDate = birthDate;
    }

    public Developer(String name, String lastName, String mail) {
        this.name = name;
        this.lastName = lastName;
        this.mail = mail;
        this.roles = new ArrayList<Role>();
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public WorkingStatus getStatus() {
        return status;
    }

    public void setStatus(WorkingStatus status) {
        this.status = status;
    }

}
