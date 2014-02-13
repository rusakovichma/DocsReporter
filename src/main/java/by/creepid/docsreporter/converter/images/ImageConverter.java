/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter.images;

import java.io.IOException;

/**
 *
 * @author rusakovich
 */
public interface ImageConverter {

    /**
     * Supported image format checking
     *
     * @param photo - photo bytes (base64 encoding)
     * @return
     */
    public boolean isSupportedImageType(byte[] photo);

    /**
     * Return image format
     *
     * @param photo - photo bytes (base64 encoding)
     * @return ImageForm
     */
    public ImageForm getImageForm(byte[] photo);

    /**
     * Primary photo checking
     *
     * @param photo - photo bytes (base64 encoding)
     * @return
     */
    public boolean isPhoto(byte[] photo);

    /**
     * Preview photos convert method
     *
     * @param photo - photo bytes (base64 encoding)
     * @return
     * @throws IOException
     */
    public byte[] convertPhotoToPreview(byte[] photo, int width, int height)
            throws IOException;

    /**
     * Convert cmyk colorstyle photo to rgb colorstyle
     *
     * @param cmykImage - image bytes im cmyk colorstyle
     * @return image bytes in RGB colorstyle
     * @throws IOException
     */
    public byte[] convertToRGB(byte[] cmykBytes)
            throws IOException;

}
