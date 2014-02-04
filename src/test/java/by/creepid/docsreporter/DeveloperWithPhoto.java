/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter;

import by.creepid.docsreporter.context.annotations.ImageField;
import java.util.Date;

/**
 *
 * @author mirash
 */
public class DeveloperWithPhoto extends Developer {

    @ImageField(bookmarks={"photo1", "photo2"})
    private byte[] photo;

    public DeveloperWithPhoto(String name, String lastName, String mail, Date birthDate, byte[] photo) {
        super(name, lastName, mail, birthDate);
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
