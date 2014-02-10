/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter.images;

/**
 *
 * @author rusakovich
 */
public enum ImageForm {

    bitmap("424d", false, "image/bmp"),
    fits("53494d504c45", false, "image/fits"),
    gif("47494638", true, "image/gif"),
    jpeg("ffd8ff", false, "image/jpeg"),
    tiff("49492a00", true, "image/tiff"),
    png("89504e47", true, "image/png"),
    unknown("", false, "application/octet-stream");

    private String magicNumbers;
    private String mimeType;
    private boolean isTransparent;

    private ImageForm(String magicNumbers, boolean isTransparent, String mimeType) {
        this.magicNumbers = magicNumbers;
        this.isTransparent = isTransparent;
        this.mimeType = mimeType;
    }

    public String getMagicNumbers() {
        return magicNumbers;
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public String getMimeType() {
        return mimeType;
    }
}
