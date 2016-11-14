/*
 * @(#)Demo.java 2/11/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import java.awt.*;
import java.io.Serializable;

/**
 */
public interface Demo extends Serializable {

    public static final int ATTRIBUTE_NONE = 0;
    public static final int ATTRIBUTE_NEW = 1;
    public static final int ATTRIBUTE_BETA = 2;
    public static final int ATTRIBUTE_UPDATED = 4;

    /**
     * Gets the name of this demo.
     *
     * @return the name of the demo.
     */
    String getName();

    /**
     * Gets the description of this demo.
     *
     * @return the description of the demo.
     */
    String getDescription();

    /**
     * Gets the product name that this demo mainly demos.
     *
     * @return the mainly demoed product name.
     */
    String getProduct();

    /**
     * Gets the main demo panel.
     *
     * @return the main demo panel.
     */
    Component getDemoPanel();

    /**
     * Gets the source code for the demo.
     *
     * @return the source code for the demo.
     */
    String[] getDemoSource();

    /**
     * Gets the folder for the source code of the demo.
     *
     * @return the folder for the source code of the demo.
     */
    String getDemoFolder();

    /**
     * Dispose the demo. It will be called when the demo is closed.
     */
    void dispose();

    /**
     * Gets the panel where user can set options of the demoing component(s).
     *
     * @return options panel.
     */
    Component getOptionsPanel();

    /**
     * Gets attributes such as new, updated, beta etc.
     *
     * @return attributes
     */
    int getAttributes();

    /**
     * Checks if the common options panel will be visible. The common options are setting locales or toggle
     * left-to-right or right-to-left which are common to all components.
     *
     * @return true or false.
     */
    boolean isCommonOptionsPaneVisible();
}
