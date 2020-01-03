package com.niek125.updateserver.handlers;

import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.models.Permission;
import com.niek125.updateserver.models.SocketMessage;

import java.util.Arrays;

public class TokenHandler implements Handler{
    private final JWTVerifier jwtVerifier;
    private final ObjectMapper mapper;

    public TokenHandler(JWTVerifier jwtVerifier, ObjectMapper mapper) {
        this.jwtVerifier = jwtVerifier;
        this.mapper = mapper;
    }

    @Override
    public String getProcessType() {
        return "token";
    }

    @Override
    public boolean validate(SocketMessage message) {
        try{
            final DocumentContext doc = JsonPath.parse(message.getPayload());
            message.getSender().setToken(jwtVerifier.verify(doc.read("$.token", String.class)));
            final Permission[] perms = mapper.readValue(message.getSender().getToken().getClaims().get("pms").asString(), Permission[].class);
            if (!Arrays.stream(perms).filter(x -> x.getProjectid().equals(doc.read("$.interest", String.class))).findFirst().isPresent()) {
                return false;
            }
            message.getSender().setInterest(doc.read("$.interest", String.class));
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public void construct(SocketMessage message) {
        //already set
    }
}
