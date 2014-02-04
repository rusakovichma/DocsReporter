/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.creepid.docsreporter.context.validation;

/**
 *
 * @author rusakovich
 */
public class ReportProcessingException extends Exception {

    public ReportProcessingException(String message) {
        super(message);
    }

    public ReportProcessingException(Throwable cause) {
        super(cause);
    }
}
