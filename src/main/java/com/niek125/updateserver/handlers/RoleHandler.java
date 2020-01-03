package com.niek125.updateserver.handlers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.models.Permission;
import com.niek125.updateserver.models.Role;
import com.niek125.updateserver.models.RoleType;
import com.niek125.updateserver.models.SocketMessage;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.regex.Pattern;

@AllArgsConstructor
public class RoleHandler implements Handler {
    private final ObjectMapper mapper;

    private boolean hasRequiredRole(DecodedJWT token, String interest, RoleType role) {
        try {
            final Permission[] perms = mapper.readValue(token.getClaims().get("pms").asString(), Permission[].class);
            final Permission projectperm = Arrays.stream(perms).filter(x -> x.getProjectid().equals(interest)).findFirst().orElse(new Permission());
            if ((projectperm.getRole().ordinal() <= RoleType.MEMBER.ordinal()) ||
                    (role == RoleType.OWNER)) {
                return false;
            }
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Override
    public String getProcessType() {
        return "role";
    }

    @Override
    public boolean validate(SocketMessage message) {
        final Role role;
        try {
            role = mapper.readValue(message.getPayload(), Role.class);
        } catch (JsonProcessingException e) {
            return false;
        }
        if (!(Pattern.compile(".{8}-.{4}-.{4}-.{4}-.{12}").matcher(role.getRoleid()).matches()) &&
                (!role.getUserid().equals(null)) &&
                (!role.getRoleid().equals(null))) {
            return false;
        }
        switch (message.getHeader().getAction()) {
            case CREATE:
                return role.getProjectid() == null;
            case UPDATE:
                return hasRequiredRole(message.getSender().getToken(), message.getSender().getInterest(), role.getRole());
            case DELETE:
                return hasRequiredRole(message.getSender().getToken(), message.getSender().getInterest(), RoleType.MEMBER);
            default:
                return false;
        }
    }

    @Override
    public void construct(SocketMessage message) {
        try {
            final Role role = mapper.readValue(message.getPayload(), Role.class);
            role.setProjectid(message.getSender().getInterest());
            message.setPayload(mapper.writeValueAsString(role));
        } catch (JsonProcessingException e) {
            message.setPayload("");
        }
    }
}
