package com.bivgroup;

import com.bivgroup.ws.ContractFirstWebServicePort;
import com.bivgroup.ws.ContractFirstWebServicePortImplService;
import com.bivgroup.ws.Faculty;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ContractFirstWebServicePortImplService service = new ContractFirstWebServicePortImplService();
        ContractFirstWebServicePort port = service.getContractFirstWebServicePortImplPort();
        System.out.println(port.getPath());

        System.out.println(port.getXML());
        System.out.println(port.getUni().getName());
        List<Faculty> faculties = port.getUni().getFaculty();
        for (Faculty faculty : faculties) {
            System.out.println(faculty.getName());
        }


    }
}
