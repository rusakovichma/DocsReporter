/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter;

import by.creepid.docsreporter.context.annotations.ImageField;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mirash
 */
public class DeveloperWithPhoto extends Developer {

    @ImageField(bookmarks = {"photo1", "photo2"}, width = 100)
    private byte[] photo;

    public DeveloperWithPhoto(String name, String lastName, String mail, Date birthDate, byte[] photo, List<Role> roles) {
        super(name, lastName, mail, birthDate, roles);
        this.photo = photo;
    }

    public DeveloperWithPhoto(String name, String lastName, String mail, Date birthDate, byte[] photo, List<Role> roles, WorkingStatus status) {
        super(name, lastName, mail, birthDate, roles, status);
        this.photo = photo;
    }

    public DeveloperWithPhoto(String name, String lastName, String mail, Date birthDate) {
        super(name, lastName, mail, birthDate);
    }

    public DeveloperWithPhoto(String name, String lastName, String mail) {
        super(name, lastName, mail);
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
