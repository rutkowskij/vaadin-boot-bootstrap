package io.thingcare.bootstrap.fe.ui.shared;

import com.vaadin.server.VaadinRequest;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.i18n.support.Translatable;
import org.vaadin.spring.i18n.support.TranslatableUI;

import java.util.Locale;

public abstract class BaseUI extends TranslatableUI implements Translatable {
    protected final I18N i18n;

    public BaseUI(I18N i18n) {
        this.i18n = i18n;
    }

    @Override
    protected void initUI(VaadinRequest vaadinRequest) {

    }

    @Override
    public void updateMessageStrings(Locale locale) {
        getPage().setTitle(i18n.get(titleCode(), locale));
    }

    abstract protected String titleCode();
}
