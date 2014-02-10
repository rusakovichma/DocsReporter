/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter.images;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.AdvancedResizeOp.UnsharpenMask;
import com.mortennobel.imagescaling.ResampleOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import org.monte.media.jpeg.CMYKJPEGImageReaderSpi;

/**
 *
 * @author rusakovich
 */
public class ImageConverterImpl implements ImageConverter {

    //image header length
    private static final int IMAGE_HEADER_BLOCK_SIZE = 30;
    //image prewiew scale
    private static final int IMAGE_CONVERT_SCALE = 200;
    //image prewiew mask
    private static final UnsharpenMask DEFAULT_UNSHARPEN_MASK = AdvancedResizeOp.UnsharpenMask.Normal;

    private UnsharpenMask unsharpenMask = DEFAULT_UNSHARPEN_MASK;

    /**
     * CMYK dynamic provider add.
     *
     * Other solution that could work, is to create a file
     * /META-INF/services/javax.imageio.spi, containing one line
     * org.monte.media.jpeg.CMYKJPEGImageReaderSpi and place it on the
     * classpath.
     *
     */
    @PostConstruct
    private static void addCMYKServiceProvider() {
        IIORegistry reg = IIORegistry.getDefaultInstance();

        ImageReaderSpi cmykSpi = new CMYKJPEGImageReaderSpi();

        if (!reg.contains(cmykSpi)) {
            reg.registerServiceProvider(cmykSpi);
        }
    }

    /**
     * Supported image format checking
     *
     * @param photo - photo bytes (base64 encoding)
     * @return
     */
    public boolean isSupportedImageType(byte[] photo) {
        boolean result = true;

        BufferedImage buffImg = null;

        try {
            buffImg = getBufferedImage(photo);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        result = (buffImg != null);
        return result;
    }

    /**
     * Return image format
     *
     * @param photo - photo bytes (base64 encoding)
     * @return ImageForm
     */
    public ImageForm getImageForm(byte[] photo) {
        String photoSubStr = photoHeaderHexRepresent(photo);

        ImageForm[] forms = ImageForm.values();

        for (ImageForm form : forms) {
            if (photoSubStr.indexOf(form.getMagicNumbers()) != -1) {
                return form;
            }
        }

        return ImageForm.unknown;
    }

    /**
     * Primary photo checking
     *
     * @param photo - photo bytes (base64 encoding)
     * @return
     */
    public boolean isPhoto(byte[] photo) {
        boolean result = (getImageForm(photo) != ImageForm.unknown);
        return result;
    }

    /**
     * Return photos header in hex
     *
     * @param photoBytes - photo bytes (base64 encoding)
     * @return
     */
    private String photoHeaderHexRepresent(byte[] photoBytes) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < IMAGE_HEADER_BLOCK_SIZE; i++) {
            String hex = Integer.toHexString(0xFF & photoBytes[i]);

            if (hex.length() == 1) {
                builder.append('0');
            }

            builder.append(hex);
        }

        return builder.toString();
    }

    /**
     * Preview photos convert method
     *
     * @param photo - photo bytes (base64 encoding)
     * @return
     * @throws IOException
     */
    public byte[] convertPhotoToPreview(byte[] photo, int imageScale) throws IOException {
        int size;

        BufferedImage result;
        BufferedImage original = this.getBufferedImage(photo);

        ImageForm format = getImageForm(photo);
        if (!format.isTransparent()) {
            original = convertToRGB(original);
        }

        String formatOutput = (format == ImageForm.jpeg) ? "jpeg" : "png";

        if (original.getTileHeight() >= original.getTileWidth()) {

            size = original.getTileWidth() * imageScale / original.getTileHeight();
            result = resizeImageWithHint(original, size, imageScale);
        } else {

            size = original.getTileHeight() * imageScale / original.getTileWidth();
            result = resizeImageWithHint(original, imageScale, size);
        }

        return this.getImageBytes(result, formatOutput);
    }

    /**
     * Image resize method
     *
     * @param originalImage
     * @param width - image width
     * @param height - image heigh
     * @return resized BufferedImage with unshapen mask
     */
    private BufferedImage resizeImageWithHint(BufferedImage originalImage, int width, int height) {
        ResampleOp resampleOp = new ResampleOp(width, height);
        resampleOp.setUnsharpenMask(DEFAULT_UNSHARPEN_MASK);

        BufferedImage rescaled = resampleOp.filter(originalImage, null);

        return rescaled;
    }

    /**
     * Convert BufferedImage to byte array
     *
     * @param result
     * @param formatOutput - image format
     * @return
     * @throws IOException
     */
    private byte[] getImageBytes(BufferedImage result, String formatOutput) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = null;

        try {
            ImageIO.write(result, formatOutput, baos);
            baos.flush();

            imageBytes = baos.toByteArray();
        } finally {
            baos.close();
        }

        return imageBytes;
    }

    /**
     * Convert photo bytes to BufferedImage
     *
     * @param photo - photo bytes (base64 encoding)
     * @return BufferedImage
     * @throws IOException
     */
    private BufferedImage getBufferedImage(byte[] photo) throws IOException {
        BufferedImage buffImg = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(photo);

        ImageInputStream iis = ImageIO.createImageInputStream(bais);
        //search for right ImageReader
        try {
            for (Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
                    iter.hasNext();) {
                ImageReader reader = iter.next();

                try {
                    reader.setInput(iis);
                    buffImg = reader.read(0);

                    if (buffImg != null) {
                        break;
                    }
                } catch (IOException e) {
                }

            }
        } finally {
            if (iis != null) {
                iis.close();
            }
        }
        return buffImg;
    }

    /**
     * Convert cmyk colorstyle photo to rgb colorstyle
     *
     * @param cmykImage - BufferedImage im cmyk colorstyle
     * @return BufferedImage in RGB colorstyle
     * @throws IOException
     */
    private BufferedImage convertToRGB(BufferedImage cmykImage) throws IOException {
        BufferedImage rgbImage = new BufferedImage(cmykImage.getWidth(),
                cmykImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        ColorConvertOp op = new ColorConvertOp(null);
        op.filter(cmykImage, rgbImage);

        return rgbImage;
    }

    /**
     * Convert cmyk colorstyle photo to rgb colorstyle
     *
     * @param cmykImage - image bytes im cmyk colorstyle
     * @return image bytes in RGB colorstyle
     * @throws IOException
     */
    public byte[] convertToRGB(byte[] cmykBytes) throws IOException {
        ImageForm format = this.getImageForm(cmykBytes);

        if (format.isTransparent()) {
            return cmykBytes;
        }

        String formatOutput = (format == ImageForm.jpeg) ? "jpeg" : "png";

        BufferedImage cmykImage = getBufferedImage(cmykBytes);
        BufferedImage rgbImage = convertToRGB(cmykImage);

        return getImageBytes(rgbImage, formatOutput);
    }

    public UnsharpenMask getUnsharpenMask() {
        return unsharpenMask;
    }

    public void setUnsharpenMask(UnsharpenMask unsharpenMask) {
        this.unsharpenMask = unsharpenMask;
    }

}
