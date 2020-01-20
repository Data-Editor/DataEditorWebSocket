package com.niek125.updateserver.events;

import com.niek125.updateserver.models.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleChangedEvent extends DataEditorEvent {
    private Role role;

    public UserRoleChangedEvent(String interest, Role role){
        super(interest);
        this.role = role;
    }
}
