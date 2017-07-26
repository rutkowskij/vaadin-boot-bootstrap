package io.thingcare.bootstrap.fe.view;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vaadin.data.Binder;
import io.thingcare.bootstrap.be.security.Role;
import io.thingcare.bootstrap.be.security.User;
import io.thingcare.bootstrap.fe.ui.design.UserEditDesign;

import java.util.List;
import java.util.Set;

public class UserEditView extends UserEditDesign {
    Binder<User> userBinder = new Binder<>(User.class);

    public UserEditView() {
        userBinder.forField(roles).withConverter(UserEditView::rolesAsList, Sets::newLinkedHashSet).bind(User::getRoles, User::setRoles);
        userBinder.bindInstanceFields(this);
    }

    private static List<Role> rolesAsList(Set<Role> roles) {
        return Lists.newArrayList(roles);
    }

    public void setUser(User user) {
        userBinder.setBean(user);
        roles.setItems(user.getRoles());
    }
}
