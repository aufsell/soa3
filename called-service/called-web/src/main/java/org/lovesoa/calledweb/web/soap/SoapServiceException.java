package org.lovesoa.calledweb.web.soap;

import javax.xml.ws.WebFault;

@WebFault(name = "SoapServiceFault", targetNamespace = "http://soap.web.calledweb.lovesoa.org/")
public class SoapServiceException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private String faultInfo;

    public SoapServiceException(String message) {
        super(message);
        this.faultInfo = message;
    }

    public SoapServiceException(String message, Throwable cause) {
        super(message, cause);
        this.faultInfo = message;
    }

    public String getFaultInfo() {
        return faultInfo;
    }
}
