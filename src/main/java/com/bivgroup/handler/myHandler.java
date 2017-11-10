package com.bivgroup.handler;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
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
                //add a soap header called "helloWorld"
//                QName qname = new QName("fromClientHandler", "helloWorld");
//                SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(qname);
//                soapHeaderElement.setActor(SOAPConstants.URI_SOAP_ACTOR_NEXT);
//                soapHeaderElement.addTextNode("hello");
//                soapMsg.saveChanges();


                QName username = new QName("digest", "auth");
                SOAPHeaderElement soapHeaderElement2 = soapHeader.addHeaderElement(username);
                soapHeaderElement2.setActor(SOAPConstants.URI_SOAP_ACTOR_NEXT);
//                soapHeaderElement2.addTextNode(createDigestRequest());
                soapHeaderElement2.addTextNode(buildNonce());
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


    private static String customMD5(String st) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // тут можно обработать ошибку
            // возникает она если в передаваемый алгоритм в getInstance(,,,) не существует
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }

    private static String createDigestRequest() {
        String rez = "";
        String hash1 = customMD5("user:com.bivgroup:password");            //"username:realm:password"
        String hash2 = customMD5("POST:/getAuth");                   //"method:digestURI
        rez = customMD5(hash1 + ":nonce:nonceCount:cnonce:qop:" + hash2);   //hash1 + ":nonce:nonceCount:cnonce:qop:" + hash2
        return rez;
    }

    public static String buildNonce(){

//        java.security.SecureRandom random = java.security.SecureRandom.getInstance("SHA1PRNG");
//        random.setSeed(System.currentTimeMillis());
//        byte[] nonceBytes = new byte[16];
//        random.nextBytes(nonceBytes);
//        String nonce = new String(org.apache.commons.codec.binary.Base64.encodeBase64(nonceBytes), "UTF-8");

        StringBuffer nonce=new StringBuffer();
        String dateTimeString = Long.toString(new Date().getTime());
        byte[] nonceByte= dateTimeString.getBytes();
        return Base64.encode(nonceByte);
    }

    public static String buildPasswordDigest(String userName, String password, String nonce, String dateTime) {
        MessageDigest sha1;
        String passwordDigest = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
            byte[] hash = MessageDigest.getInstance("SHA-1").digest(password.getBytes("UTF-8"));
            sha1.update(nonce.getBytes("UTF-8"));
            sha1.update(dateTime.getBytes("UTF-8"));
            passwordDigest = new String(Base64.encode(sha1.digest(hash)));
            sha1.reset();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return passwordDigest;
    }
//Returning invalid MAC address
        //return "E0-DB-55-A4-10-Q1";
}