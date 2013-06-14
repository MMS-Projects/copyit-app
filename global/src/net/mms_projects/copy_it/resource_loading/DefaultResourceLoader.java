package net.mms_projects.copy_it.resource_loading;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DefaultResourceLoader implements ResourceLoaderInterface {

	private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE;
	private static ResourceBundle FALLBACK_RESOURCE_BUNDLE;
	
	@Override
	public String getString(String name) {
		try {
			return getBundle().getString(name);
		} catch (MissingResourceException e1) {
			try {
				return getFallbackBundle().getString(name);
			} catch (MissingResourceException e2) {
			}
		}
		return null;
	}

	@Override
	public String getString(String name, Object... formatArgs) {
		String raw = getString(name);
		return String.format(raw, formatArgs);
	}
	
	private ResourceBundle getBundle() {
		Locale locale = Locale.getDefault();
		if (RESOURCE_BUNDLE == null) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
		}
		return RESOURCE_BUNDLE;
	}

	private ResourceBundle getFallbackBundle() {
		if (FALLBACK_RESOURCE_BUNDLE == null) {
			FALLBACK_RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		}
		return FALLBACK_RESOURCE_BUNDLE;
	}

}
