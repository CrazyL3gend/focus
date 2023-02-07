package ru.crazylegend.focus.util.permission;

import org.bukkit.permissions.Permissible;

public final class PermissionUtil {

    private PermissionUtil() {
        throw new UnsupportedOperationException();
    }

    public static boolean hasPermission(Permissible permissible, String permission) {
        return PermissionNode.node(permission).checkPermission(permissible);
    }

    public static boolean hasPermission(Permissible permissible, PermissionNode node) {
        return node.checkPermission(permissible);
    }

}
