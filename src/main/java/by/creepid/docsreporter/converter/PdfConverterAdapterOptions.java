/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

/**
 *
 * @author rusakovich
 */
public interface PdfConverterAdapterOptions {

    public static final String DEFAULT_ENCODING = "UTF-8";

    public void setEncoding(String encoding);

}
