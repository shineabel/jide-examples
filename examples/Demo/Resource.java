/*
 * @(#)Resource.java 2/27/2012
 *
 * Copyright 2002 - 2012 JIDE Software Inc. All rights reserved.
 */

import java.util.Locale;
import java.util.ResourceBundle;

public class Resource {
    static final String BASENAME = "demo";

    static final ResourceBundle RB = ResourceBundle.getBundle(BASENAME);

    public static ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(BASENAME, locale);
    }
}
