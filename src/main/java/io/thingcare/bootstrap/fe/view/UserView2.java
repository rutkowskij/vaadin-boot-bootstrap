package io.thingcare.bootstrap.fe.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import io.thingcare.bootstrap.fe.sidebar.Sections;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import javax.annotation.PostConstruct;


@SpringView(name = UserView2.VIEW_NAME)
@SideBarItem(sectionId = Sections.USER, caption = "UserView2", order = 2)
@VaadinFontIcon(VaadinIcons.USER)
public class UserView2 extends AbstractView {

    public static final String VIEW_NAME = "user_view_2";

    public UserView2() {
        setViewHeader("UserView2");
    }

    @PostConstruct
    public void init() {
        addComponent(new Label("Hello, this is user view2."));
    }

}
