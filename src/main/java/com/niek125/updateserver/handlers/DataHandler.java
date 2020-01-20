package com.niek125.updateserver.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.models.Permission;
import com.niek125.updateserver.models.RoleType;
import com.niek125.updateserver.models.SessionWrapper;
import com.niek125.updateserver.models.SocketMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@AllArgsConstructor
public class DataHandler extends Handler {
    private final Logger logger = LoggerFactory.getLogger(DataHandler.class);
    private final ObjectMapper mapper;

    @Override
    public String getProcessType() {
        return "data";
    }

    @Override
    protected boolean hasPermission(SessionWrapper sessionWrapper) {
        try {
            final Permission[] perms = mapper.readValue(sessionWrapper.getToken().getClaims().get("pms").asString(), Permission[].class);
            final Permission projectperm = Arrays.stream(perms)
                    .filter(x -> x.getProjectid().equals(sessionWrapper.getInterest()))
                    .findFirst().orElse(new Permission());
            return (projectperm.getRole().ordinal() > RoleType.GUEST.ordinal());
        } catch (JsonProcessingException e) {
            logger.error("invalid token structure: {}", e.getMessage());
            return false;
        }
    }

    @Override
    protected boolean validate(SocketMessage message) {
        try {
            final DocumentContext json = JsonPath.parse(message.getPayload());
            switch (message.getHeader().getAction()) {
                case DELETE:
                    json.read("$.rownumber");
                    break;
                case UPDATE:
                    json.read("$.row");
                    json.read("$.rownumber");
                    break;
                case CREATE:
                    json.read("$.row");
                    break;
                default:
                    logger.error("action: {} not found", message.getHeader().getAction());
            }
            return true;
        } catch (Exception e) {
            logger.debug("parse error: {}", e.getMessage());
            return false;
        }
    }

    @Override
    protected void construct(SocketMessage message, SessionWrapper sessionWrapper) {
        DocumentContext json = JsonPath.parse(message.getPayload());
        message.setPayload(json.put("$", "projectid", sessionWrapper.getInterest()).jsonString());
    }
}
