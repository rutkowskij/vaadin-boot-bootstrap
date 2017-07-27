package io.thingcare.bootstrap.fe.view.security;

import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import io.thingcare.bootstrap.be.security.User;
import io.thingcare.bootstrap.be.security.UserRepository;
import io.thingcare.bootstrap.fe.sidebar.Sections;
import io.thingcare.bootstrap.fe.ui.design.UserManagementDesign;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import javax.annotation.PostConstruct;


@Secured({"ROLE_ADMIN"})
@SpringView(name = UserManagementView.VIEW_NAME)
@SideBarItem(sectionId = Sections.ADMIN, caption = "User management", order = 1)
@VaadinFontIcon(VaadinIcons.ADD_DOCK)
public class UserManagementView extends UserManagementDesign {

    public static final String VIEW_NAME = "user_management_view";

    private final UserRepository userRepository;


    public UserManagementView(I18N i18n, UserRepository userRepository) {
        super(i18n);
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        initUserGrid();
    }

    private void initUserGrid() {
        userGrid.setItems(userRepository.findAll());
        userGrid.asSingleSelect().addValueChangeListener(this::onUserSelected);
    }

    private void onUserSelected(HasValue.ValueChangeEvent<User> event) {
        userDetailsView.setUser(event.getValue());
    }
}

