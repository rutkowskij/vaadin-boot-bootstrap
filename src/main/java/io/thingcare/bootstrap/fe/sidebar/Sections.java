package io.thingcare.bootstrap.fe.sidebar;

import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;


@SideBarSections({
        @SideBarSection(id = Sections.NO_GROUP, caption = ""),
        @SideBarSection(id = Sections.USER, caption = "User"),
        @SideBarSection(id = Sections.ADMIN, caption = "Admin")
})
@Component
public class Sections {
    public static final String NO_GROUP = "nogroup";
    public static final String USER = "user";
    public static final String ADMIN = "admin";
}
