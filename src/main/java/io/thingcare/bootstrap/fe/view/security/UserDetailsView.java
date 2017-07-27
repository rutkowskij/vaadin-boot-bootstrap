package io.thingcare.bootstrap.fe.view.security;

import com.vaadin.data.Binder;
import io.thingcare.bootstrap.be.security.User;
import io.thingcare.bootstrap.fe.ui.design.UserDetailsDesign;
import org.vaadin.spring.i18n.I18N;

public class UserDetailsView extends UserDetailsDesign {

    Binder<User> binder = new Binder<>(User.class);

    public UserDetailsView(I18N i18n) {
        super(i18n);
        binder.bindInstanceFields(this);
    }

    public void setUser(User user) {
        binder.setBean(user);
    }
}
