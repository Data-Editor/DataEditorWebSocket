package com.niek125.updateserver.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.models.*;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.regex.Pattern;

@AllArgsConstructor
public class RoleHandler extends Handler {
    private final ObjectMapper mapper;

    @Override
    public String getProcessType() {
        return "role";
    }

    @Override
    protected boolean hasPermission(SessionWrapper sessionWrapper) {
        try {
            final Permission[] perms = mapper.readValue(sessionWrapper.getToken().getClaims().get("pms").asString(), Permission[].class);
            final Permission projectperm = Arrays.stream(perms).filter(x -> x.getProjectid().equals(sessionWrapper.getInterest())).findFirst().orElse(new Permission());
            return  !((projectperm.getRole().ordinal() <= RoleType.MEMBER.ordinal()) ||
                    (projectperm.getRole() == RoleType.OWNER));
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Override
    protected boolean validate(SocketMessage message) {
        final Role role;
        try {
            role = mapper.readValue(message.getPayload(), Role.class);
        } catch (JsonProcessingException e) {
            return false;
        }
        return Pattern.compile(".{8}-.{4}-.{4}-.{4}-.{12}").matcher(role.getRoleid()).matches() &&
                (role.getUserid() != null) &&
                (role.getRoleid() != null);
    }

    @Override
    protected void construct(SocketMessage message, SessionWrapper sessionWrapper) {
        try {
            final Role role = mapper.readValue(message.getPayload(), Role.class);
            role.setProjectid(sessionWrapper.getInterest());
            message.setPayload(mapper.writeValueAsString(role));
        } catch (JsonProcessingException e) {
            message.setPayload("");
        }
    }
}
