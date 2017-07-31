package io.thingcare.bootstrap.fe.sidebar;

import com.google.common.eventbus.Subscribe;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import io.thingcare.bootstrap.be.security.User;
import io.thingcare.bootstrap.fe.component.ProfilePreferencesWindow;
import io.thingcare.bootstrap.fe.event.MainEvent;
import io.thingcare.bootstrap.fe.event.MainEventBus;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.sidebar.SideBarItemDescriptor;
import org.vaadin.spring.sidebar.SideBarSectionDescriptor;
import org.vaadin.spring.sidebar.SideBarUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.thingcare.bootstrap.be.security.SecurityContextUtils.getUser;


@SpringComponent
@UIScope
public class VaadinSideBar extends CustomComponent {

    private static final String VALO_MENU_VISIBLE = "valo-menu-visible";
    private static final String VALO_MENU_TOGGLE = "valo-menu-toggle";
    private final VaadinSecurity vaadinSecurity;
    private final SideBarUtils sideBarUtils;
    private SectionComponentFactory<CssLayout> sectionComponentFactory;
    private ItemComponentFactory itemComponentFactory;
    private ItemFilter itemFilter;
    private MenuBar.MenuItem settingsItem;

    public VaadinSideBar(SideBarUtils sideBarUtils, VaadinSecurity vaadinSecurity) {
        setPrimaryStyleName(ValoTheme.MENU_ROOT);
        setSizeUndefined();
        this.sideBarUtils = sideBarUtils;
        this.vaadinSecurity = vaadinSecurity;
    }

    @Override
    public void attach() {
        super.attach();
        CssLayout menuPart = createMenuPart();
        setCompositionRoot(menuPart);

        CssLayout menuItems = createMenuItems(menuPart);
        menuPart.addComponent(menuItems);

        CssLayout systemItems = createSystemItems();
        menuItems.addComponent(systemItems);
    }

    private CssLayout createMenuPart() {
        final CssLayout layout = new CssLayout();
        layout.setId("menuPart");
        layout.addStyleName(ValoTheme.MENU_PART);

        final VerticalLayout top = new VerticalLayout();
        top.setId("top");
        top.setSpacing(false);
        top.addStyleName(ValoTheme.MENU_TITLE);
        Component title = buildTitle();
        top.addComponent(title);
        Component userMenu = buildUserMenu();
        top.addComponent(userMenu);
        top.setExpandRatio(userMenu, 1.0f);

        layout.addComponent(top);

        final Button showMenu = new Button("Menu", event -> {
            if (layout.getStyleName().contains(VALO_MENU_VISIBLE)) {
                layout.removeStyleName(VALO_MENU_VISIBLE);
            } else {
                layout.addStyleName(VALO_MENU_VISIBLE);
            }
        });

        showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
        showMenu.addStyleName(ValoTheme.BUTTON_TINY);
        showMenu.addStyleName(VALO_MENU_TOGGLE);
        showMenu.setIcon(VaadinIcons.MENU);

        layout.addComponent(showMenu);

        return layout;
    }

    private Component buildTitle() {
        Label logo = new Label("<strong>Bootstrap</strong> project", ContentMode.HTML);
        logo.setSizeUndefined();

        return logo;
    }


    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.setWidth(100, Unit.PERCENTAGE);

        User currentUser = getUser();
        settings.addStyleName("user-menu");

        settingsItem = settings.addItem("",
                new ThemeResource("img/profile-pic-300px.jpg"), null);
        settingsItem.addItem("Edit Profile", (MenuBar.Command) selectedItem -> ProfilePreferencesWindow.open(currentUser, false));
        settingsItem.addItem("Preferences", (MenuBar.Command) selectedItem -> ProfilePreferencesWindow.open(currentUser, true));
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", (MenuBar.Command) selectedItem -> MainEventBus.post(new MainEvent.UserLoggedOutEvent()));


        updateCurrentUser(currentUser);
        return settings;
    }

    @Subscribe
    public void updateUserName(final MainEvent.ProfileUpdatedEvent event) {
        updateCurrentUser(getUser());
    }

    private void updateCurrentUser(User currentUser) {
        settingsItem.setText(loggedUserCaption(currentUser));
    }

    private String loggedUserCaption(User currentUser) {
        return currentUser.getEmail();
    }

    private CssLayout createMenuItems(CssLayout menuPart) {
        CssLayout menuItems = new CssLayout();
        menuItems.setPrimaryStyleName("valo-menuitems");
        for (SideBarSectionDescriptor section : sideBarUtils.getSideBarSections(getUI().getClass())) {
            createSection(menuPart, menuItems, section, sideBarUtils.getSideBarItems(section));
        }
        return menuItems;
    }

    private CssLayout createSystemItems() {
        Button logout = new Button("Logout", event -> vaadinSecurity.logout());
        logout.setPrimaryStyleName(ValoTheme.MENU_ITEM);
        logout.setIcon(VaadinIcons.SIGN_OUT);

        CssLayout systemItems = new CssLayout();
        systemItems.addStyleName("valo-systemitems");
        systemItems.addComponent(logout);
        return systemItems;
    }


    private void createSection(CssLayout menuPart, CssLayout menuItems, SideBarSectionDescriptor section, Collection<SideBarItemDescriptor> items) {
        if (items.isEmpty()) {
            return;
        }
        if (getItemFilter() == null) {
            getSectionComponentFactory().createSection(menuPart, menuItems, section, items);
        } else {
            List<SideBarItemDescriptor> passedItems = new ArrayList<>();
            for (SideBarItemDescriptor candidate : items) {
                if (getItemFilter().passesFilter(candidate)) {
                    passedItems.add(candidate);
                }
            }
            if (!passedItems.isEmpty()) {
                getSectionComponentFactory().createSection(menuPart, menuItems, section, passedItems);
            }
        }
    }

    protected SectionComponentFactory<CssLayout> getSectionComponentFactory() {
        if (sectionComponentFactory == null) {
            sectionComponentFactory = createDefaultSectionComponentFactory();
        }
        sectionComponentFactory.setItemComponentFactory(getItemComponentFactory());
        return sectionComponentFactory;
    }

    protected void setSectionComponentFactory(SectionComponentFactory<CssLayout> sectionComponentFactory) {
        this.sectionComponentFactory = sectionComponentFactory;
    }

    protected ItemComponentFactory getItemComponentFactory() {
        if (itemComponentFactory == null) {
            itemComponentFactory = createDefaultItemComponentFactory();
        }
        return itemComponentFactory;
    }

    public ItemFilter getItemFilter() {
        return itemFilter;
    }

    public void setItemFilter(ItemFilter itemFilter) {
        if (isAttached()) {
            throw new IllegalStateException("An ItemFilter cannot be set when the SideBar is attached");
        }
        this.itemFilter = itemFilter;
    }

    protected SectionComponentFactory<CssLayout> createDefaultSectionComponentFactory() {
        return new DefaultSectionComponentFactory();
    }

    protected ItemComponentFactory createDefaultItemComponentFactory() {
        return new DefaultItemComponentFactory();
    }

    public interface SectionComponentFactory<CR extends ComponentContainer> {
        void setItemComponentFactory(ItemComponentFactory itemComponentFactory);

        void createSection(CR menuPart, CR menuItems, SideBarSectionDescriptor descriptor, Collection<SideBarItemDescriptor> itemDescriptors);
    }

    public interface ItemComponentFactory {
        Component createItemComponent(CssLayout layout, SideBarItemDescriptor descriptor);
    }

    public interface ItemFilter {
        boolean passesFilter(SideBarItemDescriptor descriptor);
    }

    static class ViewItemButton extends ItemButton implements ViewChangeListener {

        private static final String STYLE_SELECTED = "selected";
        private final String viewName;
        private final CssLayout menuPart;

        ViewItemButton(CssLayout menuPart, SideBarItemDescriptor.ViewItemDescriptor descriptor) {
            super(descriptor);
            this.menuPart = menuPart;
            this.viewName = descriptor.getViewName();
        }

        @Override
        public void attach() {
            super.attach();
            if (getUI().getNavigator() == null) {
                throw new IllegalStateException("Please configure the Navigator before you attach the SideBar to the UI");
            }
            getUI().getNavigator().addViewChangeListener(this);
        }

        @Override
        public boolean beforeViewChange(ViewChangeEvent event) {
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            if (event.getViewName().equals(viewName)) {
                addStyleName(STYLE_SELECTED);
            } else {
                removeStyleName(STYLE_SELECTED);
            }
            menuPart.removeStyleName(VALO_MENU_VISIBLE);
        }
    }

    static class ItemButton extends Button {

        ItemButton(final SideBarItemDescriptor descriptor) {
            setPrimaryStyleName(ValoTheme.MENU_ITEM);
            setCaption(descriptor.getCaption());
            setIcon(descriptor.getIcon());
            setId(descriptor.getItemId());
            setDisableOnClick(true);
            addClickListener((ClickListener) event -> {
                try {
                    descriptor.itemInvoked(getUI());
                } finally {
                    setEnabled(true);
                }
            });
        }
    }

    public class DefaultSectionComponentFactory implements SectionComponentFactory<CssLayout> {

        private ItemComponentFactory itemComponentFactory;

        @Override
        public void setItemComponentFactory(ItemComponentFactory itemComponentFactory) {
            this.itemComponentFactory = itemComponentFactory;
        }

        @Override
        public void createSection(CssLayout menuPart, CssLayout menuItems, SideBarSectionDescriptor descriptor, Collection<SideBarItemDescriptor> itemDescriptors) {
            if (!descriptor.getId().equals(Sections.NO_GROUP)) {
                Label header = new Label();
                header.setValue(descriptor.getCaption());
                header.setSizeUndefined();
                header.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
                menuItems.addComponent(header);
            }
            for (SideBarItemDescriptor item : itemDescriptors) {
                menuItems.addComponent(itemComponentFactory.createItemComponent(menuPart, item));
            }
        }
    }

    public class DefaultItemComponentFactory implements ItemComponentFactory {
        @Override
        public Component createItemComponent(CssLayout menuPart, SideBarItemDescriptor descriptor) {
            if (descriptor instanceof SideBarItemDescriptor.ViewItemDescriptor) {
                return new ViewItemButton(menuPart, (SideBarItemDescriptor.ViewItemDescriptor) descriptor);
            } else {
                return new ItemButton(descriptor);
            }
        }
    }
}
