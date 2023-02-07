package ru.crazylegend.focus.util.permission;

import org.bukkit.permissions.Permissible;

public interface PermissionNode {

    static PermissionNode node(String permission) {
        return new PermissionNodeImpl(permission);
    }

    String getPermission();

    default String[] getDividedPermissions() {
        return getPermission().split("\\.");
    }

    default boolean checkPermission(Permissible permissible) {
        if (permissible.hasPermission("*")) {
            return true;
        }
        final String[] permissions = getDividedPermissions();
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < permissions.length; i++) {
            final String addition = permissions[i];

            builder.append(i == (permissions.length - 1) ? addition : addition + ".");
            if (permissible.hasPermission(builder.toString())) {
                return true;
            }
        }
        return false;
    }
}
