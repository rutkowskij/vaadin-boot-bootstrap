package io.thingcare.bootstrap.fe.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.themes.ValoTheme;
import io.thingcare.bootstrap.be.security.User;
import io.thingcare.bootstrap.be.security.UserRepository;
import io.thingcare.bootstrap.fe.sidebar.Sections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import javax.annotation.PostConstruct;
import java.util.List;


@Secured({"ROLE_ADMIN"})
@SpringView(name = UserManagementView.VIEW_NAME)
@SideBarItem(sectionId = Sections.ADMIN, caption = "User management", order = 1)
@VaadinFontIcon(VaadinIcons.ADD_DOCK)
public class UserManagementView extends AbstractView {

    public static final String VIEW_NAME = "user_management_view";

    private final UserRepository userRepository;

    private Button newBtn = new Button("New");
    private Button delBtn = new Button("Delete");

    @Autowired
    public UserManagementView(UserRepository userRepository) {
        this.userRepository = userRepository;
        setSizeFull();
        setViewHeader("User management", VaadinIcons.ADD_DOCK);

        newBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newBtn.setIcon(VaadinIcons.PLUS_CIRCLE);

        delBtn.addStyleName(ValoTheme.BUTTON_DANGER);
        delBtn.setIcon(VaadinIcons.MINUS_CIRCLE);

        addToolbarComponents(newBtn, delBtn);
    }

    @PostConstruct
    public void init() {
        List<User> users = userRepository.findAll();

        Grid<User> grid = new Grid<>();
        grid.getEditor().setEnabled(true);
        grid.setSizeFull();
        grid.setItems(users);
        grid.addColumn(User::getUsername).setCaption("Name");
        grid.addColumn(User::getPassword).setCaption("Password");

        addComponent(grid);
        setExpandRatio(grid, 1f);
    }

}

