package io.thingcare.bootstrap.fe.ui.design.security;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import io.thingcare.bootstrap.be.security.User;
import io.thingcare.bootstrap.fe.view.security.UserDetailsView;
import io.thingcare.bootstrap.fe.view.shared.BaseView;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.i18n.I18N;

import java.util.stream.Collectors;

public class UserManagementDesign extends BaseView {
    protected TextField search = new TextField();
    protected Button addButton = new Button();
    protected Button editButton = new Button();
    protected Button disableButton = new Button();
    protected Grid<User> userGrid = new Grid<>();

    protected UserDetailsView userDetailsView;
    protected HorizontalLayout topBar;
    protected VerticalLayout mainLayout;
    protected CssLayout gridLayout;

    public UserManagementDesign(I18N i18n, EventBus.ViewEventBus eventBus) {
        super(i18n, eventBus);
        this.userDetailsView = new UserDetailsView(i18n, eventBus);
        this.userDetailsView.setEnabled(false);
        this.userDetailsView.setVisible(false);

        designSearch();
        designAddButton();
        designEditButton();
        designDisableButton();
        designUserGrid();

        HorizontalLayout searchLayout = designSearchLayout();
        topBar = designTopBar(searchLayout);
        gridLayout = designGridLayout();

        mainLayout = designMainLayout(topBar, gridLayout, userDetailsView);

        setSizeFull();
        addComponent(mainLayout);
    }


    private VerticalLayout designMainLayout(HorizontalLayout topBar, CssLayout gridLayout, UserDetailsView userDetailsView) {
        VerticalLayout main = new VerticalLayout(topBar, gridLayout, userDetailsView);
        main.setExpandRatio(gridLayout, 1f);
        main.setResponsive(true);
        main.setSizeFull();
        return main;
    }

    private CssLayout designGridLayout() {
        CssLayout content = new CssLayout(userGrid);
        content.setSizeFull();
        return content;
    }


    private void designUserGrid() {
        userGrid.setSizeFull();
        userGrid.addStyleName(ValoTheme.LABEL_HUGE);
        userGrid.addColumn(User::getEmail).setCaption("Email");
        userGrid.addColumn(User::getFirstName).setCaption("First name");
        userGrid.addColumn(User::getLastName).setCaption("Last name");
        userGrid.addColumn(User::getRoles, roles ->
                roles.stream().map(role -> role.getType().name()).collect(Collectors.joining("\n"))).setCaption("Roles");
    }

    private HorizontalLayout designSearchLayout() {
        HorizontalLayout searchLayout = new HorizontalLayout(search);
        searchLayout.setWidth(100, Unit.PERCENTAGE);
        searchLayout.setComponentAlignment(search, Alignment.MIDDLE_LEFT);
        return searchLayout;
    }

    private HorizontalLayout designTopBar(HorizontalLayout searchLayout) {
        HorizontalLayout topBar = new HorizontalLayout(searchLayout, addButton, editButton, disableButton);
        topBar.setWidth(100, Unit.PERCENTAGE);
        topBar.setHeight(50, Unit.PIXELS);
        topBar.setExpandRatio(searchLayout, 1.0f);
        topBar.setComponentAlignment(searchLayout, Alignment.MIDDLE_LEFT);
        topBar.setComponentAlignment(addButton, Alignment.MIDDLE_RIGHT);
        topBar.setComponentAlignment(editButton, Alignment.MIDDLE_RIGHT);
        topBar.setComponentAlignment(disableButton, Alignment.MIDDLE_RIGHT);
        return topBar;
    }

    private void designAddButton() {
        addButton.setIcon(VaadinIcons.PLUS);
        addButton.setStyleName(ValoTheme.BUTTON_LARGE);
        addButton.setStyleName(ValoTheme.BUTTON_FRIENDLY, true);
        addButton.setCaption("Add new");
        addButton.setResponsive(true);
    }

    private void designEditButton() {
        disableButton.setEnabled(false);
        editButton.setIcon(VaadinIcons.EDIT);
        editButton.setStyleName(ValoTheme.BUTTON_LARGE);
        editButton.setStyleName(ValoTheme.BUTTON_FRIENDLY, true);
        editButton.setCaption("Edit");
        editButton.setResponsive(true);
    }

    private void designDisableButton() {
        disableButton.setEnabled(false);
        disableButton.setIcon(VaadinIcons.LOCK);
        disableButton.setStyleName(ValoTheme.BUTTON_LARGE);
        disableButton.setStyleName(ValoTheme.BUTTON_DANGER, true);
        disableButton.setResponsive(true);
        disableButton.setCaption("Disable");
    }

    private void designSearch() {
        search.setId("userSearch");
        search.setPlaceholder("Search");
        search.setStyleName(ValoTheme.TEXTFIELD_LARGE);
        search.setWidth(80, Unit.PERCENTAGE);
    }
}
