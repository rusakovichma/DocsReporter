/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.context;

import fr.opensagres.xdocreport.template.IContext;

/**
 *
 * @author rusakovich
 */
public interface ContextProcessor extends IContext {

    public void setContext(IContext context);

}
