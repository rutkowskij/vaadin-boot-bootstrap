package io.thingcare.bootstrap.fe.sidebar;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.sidebar.SideBarItemDescriptor;
import org.vaadin.spring.sidebar.SideBarSectionDescriptor;
import org.vaadin.spring.sidebar.SideBarUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
        layout.addStyleName(ValoTheme.MENU_PART);

        final Label title = new Label("<h3>Vaadin <strong>Project</strong></h3>", ContentMode.HTML);
        title.setSizeUndefined();

        final HorizontalLayout top = new HorizontalLayout();
        top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        top.addStyleName(ValoTheme.MENU_TITLE);
        top.addComponent(title);

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
            addClickListener(new ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    try {
                        descriptor.itemInvoked(getUI());
                    } finally {
                        setEnabled(true);
                    }
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
