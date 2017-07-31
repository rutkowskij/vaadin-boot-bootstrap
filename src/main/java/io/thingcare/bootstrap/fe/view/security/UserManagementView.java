package io.thingcare.bootstrap.fe.view.security;

import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import io.thingcare.bootstrap.be.security.User;
import io.thingcare.bootstrap.be.security.UserRepository;
import io.thingcare.bootstrap.fe.sidebar.Sections;
import io.thingcare.bootstrap.fe.ui.design.security.UserManagementDesign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import javax.annotation.PostConstruct;

@Slf4j
@Secured({"ADMIN"})
@SpringView(name = UserManagementView.VIEW_NAME)
@SideBarItem(sectionId = Sections.ADMIN, caption = "User management", order = 1)
@VaadinFontIcon(VaadinIcons.ADD_DOCK)
public class UserManagementView extends UserManagementDesign {

    public static final String VIEW_NAME = "user_management_view";

    private final UserRepository userRepository;


    public UserManagementView(I18N i18n, EventBus.ViewEventBus eventBus, UserRepository userRepository) {
        super(i18n, eventBus);
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        initUserGrid();
    }

    private void initUserGrid() {
        userGrid.setItems(userRepository.findAll());
        userGrid.asSingleSelect().addValueChangeListener(this::onUserSelected);

        addButton.addClickListener(this::onAddClick);
        editButton.addClickListener(this::onEditClick);
    }

    private void onEditClick(Button.ClickEvent clickEvent) {
        enableUserDetailsView();

        disableGridView();
    }

    private void enableGridView() {
        gridLayout.setVisible(true);
        topBar.setVisible(true);
    }

    private void disableGridView() {
        gridLayout.setVisible(false);
        topBar.setVisible(false);
    }

    private void enableUserDetailsView() {
        userDetailsView.setEnabled(true);
        userDetailsView.setVisible(true);
    }

    private void disableUserDetailsView() {
        userDetailsView.setEnabled(false);
        userDetailsView.setVisible(false);
    }

    private void onAddClick(Button.ClickEvent clickEvent) {
        userDetailsView.setUser(new User());
        enableUserDetailsView();

        disableGridView();
    }

    private void onUserSelected(HasValue.ValueChangeEvent<User> event) {
        User selectedlUser = event.getValue();
        userDetailsView.setUser(event.getValue());

        if (selectedlUser != null) {
            editButton.setEnabled(true);
            disableButton.setEnabled(selectedlUser.isEnabled());
        } else {
            editButton.setEnabled(false);
            disableButton.setEnabled(false);
        }
    }

    @EventBusListenerMethod
    private void onCancelEvent(AddEditUserCancelledEvent event) {
        disableUserDetailsView();
        enableGridView();
    }
}

