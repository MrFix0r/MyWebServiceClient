package com.bivgroup.handler;

import java.io.IOException;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class myHandler implements SOAPHandler<SOAPMessageContext>{

    public boolean handleMessage(SOAPMessageContext context) {
        System.out.println("Client : handleMessage() Begin");
        Boolean outBoundProperty  = (Boolean)
                context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        //If its an outgoing message from client, then outBoundProperty will be true
        if(outBoundProperty){
            try{
                SOAPMessage soapMsg = context.getMessage();
                SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
                SOAPHeader soapHeader = soapEnv.getHeader();

                //if no header, add the header
                if (soapHeader == null){
                    soapHeader = soapEnv.addHeader();
                }
                //add a soap header called "macAddress"
                QName qname = new QName("fromClientHandler", "helloWorld");
                SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(qname);

                soapHeaderElement.setActor(SOAPConstants.URI_SOAP_ACTOR_NEXT);
                //Add MAC address to SOAP header
                soapHeaderElement.addTextNode("hello");
                soapMsg.saveChanges();


                //Output the message to console
                soapMsg.writeTo(System.out);

            }catch(SOAPException e){
                System.err.println(e);
            }catch(IOException e){
                System.err.println(e);
            }

        }

        //Returning true makes other handler chain to continue the execution
        return true;
    }

    public boolean handleFault(SOAPMessageContext context) {
        System.out.println("Client : handleFault() Begin");
        return true;
    }

    public void close(MessageContext context) {
        System.out.println("Client : close() Begin");

    }

    public Set<QName> getHeaders() {
        System.out.println("Client : getHeaders() Begin");
        return null;
    }

    private String getMACAddress(){
        //Write a java code to get client machine MAC address and return it dynamically
        //Returning valid MAC address
        return "E0-DB-55-A4-10-Z4";

//Returning invalid MAC address
        //return "E0-DB-55-A4-10-Q1";
    }
}