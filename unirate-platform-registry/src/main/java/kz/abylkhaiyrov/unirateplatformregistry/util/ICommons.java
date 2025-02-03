package kz.abylkhaiyrov.unirateplatformregistry.util;

import kz.abylkhaiyrov.unirateplatformregistry.entity.Role;

public interface ICommons {

    static Role mapUserRole(String role) {
        if(role == null){
            return null;
        }

        return Role.valueOf(role.trim().toUpperCase());
    }

}
