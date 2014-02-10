/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context.validation;

import org.springframework.validation.Errors;

/**
 *
 * @author rusakovich
 */
public class ReportProcessingException extends Exception {

    private Errors errors;

    public ReportProcessingException(Errors errors) {
        this.errors = errors;
    }

    public ReportProcessingException(String message) {
        super(message);
    }

    public ReportProcessingException(Throwable cause) {
        super(cause);
    }

    public ReportProcessingException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }

    public ReportProcessingException(Throwable cause, Errors errors) {
        super(cause);
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }

}
