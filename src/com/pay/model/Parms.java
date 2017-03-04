package com.pay.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect(JsonMethod.FIELD)  
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Parms {
    @JsonProperty("Sign") 
    private String Sign;
    @JsonProperty("PhoneNo") 
    private String PhoneNo;
    @JsonProperty("Opration") 
    private String Opration;
    @JsonProperty("AuthorizeAccountNo") 
    private String AuthorizeAccountNo;
    
    public Parms() {
        super();
        // TODO Auto-generated constructor stub
    }
    public String getSign() {
        return Sign;
    }
    public void setSign(String sign) {
        Sign = sign;
    }
    public String getPhoneNo() {
        return PhoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }
    public String getOpration() {
        return Opration;
    }
    public void setOpration(String opration) {
        Opration = opration;
    }
    public String getAuthorizeAccountNo() {
        return AuthorizeAccountNo;
    }
    public void setAuthorizeAccountNo(String authorizeAccountNo) {
        AuthorizeAccountNo = authorizeAccountNo;
    }
    @Override
    public String toString() {
        return "Parms [Sign=" + Sign + ", PhoneNo=" + PhoneNo + ", Opration=" + Opration
                + ", AuthorizeAccountNo=" + AuthorizeAccountNo + "]";
    }

}
