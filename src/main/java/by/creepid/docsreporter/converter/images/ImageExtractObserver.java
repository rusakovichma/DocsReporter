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
public interface ImageExtractObserver {

    public void getImage(byte[] content, String path);

}
