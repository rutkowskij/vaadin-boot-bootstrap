package io.thingcare.bootstrap.fe.view.security;

import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import io.thingcare.bootstrap.be.security.User;
import io.thingcare.bootstrap.fe.ui.design.security.UserDetailsDesign;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.i18n.I18N;

@Slf4j
public class UserDetailsView extends UserDetailsDesign {

    Binder<User> binder = new Binder<>(User.class);

    public UserDetailsView(I18N i18n, EventBus.ViewEventBus eventBus) {
        super(i18n, eventBus);
        binder.bindInstanceFields(this);

        cancelButton.addClickListener(this::onCancelClick);
    }

    private void onCancelClick(Button.ClickEvent clickEvent) {
        eventBus.publish(EventScope.VIEW, this, new AddEditUserCancelledEvent());
    }

    public void setUser(User user) {
        binder.setBean(user);
    }

}
