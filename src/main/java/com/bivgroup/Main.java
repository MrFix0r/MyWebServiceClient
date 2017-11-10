package com.bivgroup;

import com.bivgroup.ws.ContractFirstWebServicePort;
import com.bivgroup.ws.ContractFirstWebServicePortImplService;
import com.bivgroup.ws.Faculty;
import com.bivgroup.ws.University;

import javax.jws.HandlerChain;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.util.*;

public class Main {

    private static ContractFirstWebServicePort port;

    public static void main(String[] args) {

        ContractFirstWebServicePortImplService service = new ContractFirstWebServicePortImplService();
        port = service.getContractFirstWebServicePortImplPort();

//        System.out.println(port.getXML());

        University uni = port.getUni();
        System.out.println(uni.getName());

        List<Faculty> faculties = uni.getFaculty();
        for (Faculty faculty : faculties) {
            System.out.println(faculty.getName());
        }

//        System.out.println(port.getDB());                             //база сейчас пустая - не сработает

        //sending request before special AUTH Headers
        System.out.println(port.getPath());
        //adding special Headers
        trySendAuthRequest();                       //HTTP auth
        //sending request after addition AUTH Headers
        System.out.println(port.getPath());

    }
    public static void trySendAuthRequest() {

        Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();

        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("Username", Collections.singletonList("user"));
        headers.put("Password", Collections.singletonList("password"));
        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
    }
}
