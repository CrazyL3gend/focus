package ru.crazylegend.focus.util.permission;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class PermissionNodeImpl implements PermissionNode {

    private String permission;
    private String[] divided;

    PermissionNodeImpl(String permission) {
        this.permission = permission;
        this.divided = PermissionNode.super.getDividedPermissions();
    }

    @Override
    public String[] getDividedPermissions() {
        return divided;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionNodeImpl that = (PermissionNodeImpl) o;
        return new EqualsBuilder().append(permission, that.permission).append(divided, that.divided).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(permission).append(divided).toHashCode();
    }
}
