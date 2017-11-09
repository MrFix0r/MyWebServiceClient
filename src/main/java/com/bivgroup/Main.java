package com.bivgroup;

import com.bivgroup.ws.ContractFirstWebServicePort;
import com.bivgroup.ws.ContractFirstWebServicePortImplService;
import com.bivgroup.ws.Faculty;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.util.*;

public class Main {
    private static ContractFirstWebServicePort port;
    private static final String WS_URL = "\t\n" +
            "http://dubna01:8080/ContractFirstWebService-1.0-SNAPSHOT/ContractFirstWebServicePortImpl?wsdl";

    public static void main(String[] args) {
        ContractFirstWebServicePortImplService service = new ContractFirstWebServicePortImplService();
        port = service.getContractFirstWebServicePortImplPort();
        System.out.println(port.getPath());

        System.out.println(port.getXML());
        System.out.println(port.getUni().getName());
        List<Faculty> faculties = port.getUni().getFaculty();
        for (Faculty faculty : faculties) {
            System.out.println(faculty.getName());
        }

        System.out.println(port.getDB());

        trySendAuthRequest();
        System.out.println(port.getPath());

    }
    public static void trySendAuthRequest() {

        Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
//        req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WS_URL);

        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("Username", Collections.singletonList("user"));
        headers.put("Password", Collections.singletonList("password"));
        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
    }
}
