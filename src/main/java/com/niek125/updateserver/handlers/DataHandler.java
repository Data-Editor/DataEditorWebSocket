package com.niek125.updateserver.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.models.Permission;
import com.niek125.updateserver.models.RoleType;
import com.niek125.updateserver.models.SocketMessage;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class DataHandler implements Handler {
    private final ObjectMapper mapper;

    @Override
    public String getProcessType() {
        return "data";
    }

    @Override
    public boolean validate(SocketMessage message) {
        try {
            final Permission[] perms = mapper.readValue(message.getSender().getToken().getClaims().get("pms").asString(), Permission[].class);
            final Permission projectperm = Arrays.stream(perms).filter(x -> x.getProjectid().equals(message.getSender().getInterest())).findFirst().orElse(new Permission());
            if (projectperm.getRole().ordinal() <= RoleType.GUEST.ordinal()) {
                return false;
            }
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
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void construct(SocketMessage message) {
    }
}
