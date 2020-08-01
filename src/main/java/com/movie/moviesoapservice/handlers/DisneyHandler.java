/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movie.moviesoapservice.handlers;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.w3c.dom.NodeList;

/**
 *
 * @author Marina
 */
public class DisneyHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public Set<QName> getHeaders() {
        return new HashSet<QName>();
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        SOAPMessage message = context.getMessage();
        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (!outbound) {
            //only inbound messages are scanned
            try {
                NodeList childElements = message.getSOAPBody().getElementsByTagName("name");
                if (childElements.item(0) != null) {
                    if (childElements.item(0).getTextContent().contains("Disney")) {
                        
                        SOAPBody body = message.getSOAPBody();
                        body.removeContents();
                        QName returnElement = new QName("", "uploadResponse", "");
                        QName productIDElement = new QName("", "length", "");
        ///doesnt work nice ..have to  figure out proper namespace and staff
                        SOAPElement returnSOAPElement = body.addBodyElement(returnElement);
                        SOAPElement  lengthSOAPElement = returnSOAPElement.addChildElement( productIDElement);
                        lengthSOAPElement.addTextNode(Integer.toString(-1));
                   
                        
                        return false;
                    }
                }
            } catch (SOAPException ex) {
                Logger.getLogger(DisneyHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        

        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }

    @Override
    public void close(MessageContext context) {
    }

}
