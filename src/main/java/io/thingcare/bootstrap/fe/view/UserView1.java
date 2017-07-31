package io.thingcare.bootstrap.fe.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import io.thingcare.bootstrap.fe.sidebar.Sections;
import io.thingcare.bootstrap.fe.view.shared.BaseView;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import javax.annotation.PostConstruct;

@SpringView(name = UserView1.VIEW_NAME)
@SideBarItem(sectionId = Sections.USER, caption = "UserView1", order = 1)
@VaadinFontIcon(VaadinIcons.ADJUST)
public class UserView1 extends BaseView {

    public static final String VIEW_NAME = "user_view_1";

    public UserView1(I18N i18n, EventBus.ViewEventBus eventBus) {
        super(i18n, eventBus);
        setViewHeader("UserView1", VaadinIcons.ADJUST);
    }

    @PostConstruct
    public void init() {
        addComponent(new Label("Hello, this is user view1."));
    }

}
