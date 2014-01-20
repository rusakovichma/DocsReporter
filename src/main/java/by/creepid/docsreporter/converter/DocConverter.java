/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

/**
 *
 * @author rusakovich
 * @param <S>
 * @param <E>
 */
public interface DocConverter<S extends DocFormat, E extends DocFormat> {

    public void convert(String sourcePath, String targetPath)
            throws Exception;

    public E getTargetFormat();

    public S getSourceFormat();

}
