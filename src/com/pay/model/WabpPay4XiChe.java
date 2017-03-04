package com.pay.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.codehaus.jackson.annotate.JsonAutoDetect;  
import org.codehaus.jackson.annotate.JsonMethod;  
import org.codehaus.jackson.annotate.JsonProperty;  
import org.codehaus.jackson.map.annotate.JsonSerialize;  
@JsonAutoDetect(JsonMethod.FIELD)  
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL) 

public class WabpPay4XiChe implements Serializable {
    /**
     * @Fields serialVersionUID : TODO
     */
    private static final long serialVersionUID = -1022430156547751554L;
    @JsonProperty("TypeName")   
    private String typeName;
    @JsonProperty("Method") 
    private String method;
    @JsonProperty("Format") 
    private String format;
    @JsonProperty("Token") 
    private String token;
    @JsonProperty("TimeSpan") 
    private String timeSpan;
    @JsonProperty("Parms") 
    private Parms parms;
    
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getTimeSpan() {
        return timeSpan;
    }
    public void setTimeSpan(String timeSpan) {
        this.timeSpan = timeSpan;
    }
    public Parms getParms() {
        return parms;
    }
    public void setParms(Parms parms) {
        this.parms = parms;
    }
    
    
}
