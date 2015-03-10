/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmf.dsmapi.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author ecus6396
 */
public final class Bundle {

    private static final String BUNDLE_NAME = "properties.server"; //$NON-NLS-1$

    private Bundle() {
    }

    /**
     * Return the value of the key If this key has no value, returns '!' + key +
     * '!'
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        try {
            return ResourceBundle.getBundle(BUNDLE_NAME).getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }

    }

    /**
     * Return the value of the key If this key has no value, returns null
     *
     * @param key
     * @return
     */
    public static String getExactString(String key) {
        try {
            return ResourceBundle.getBundle(BUNDLE_NAME).getString(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    /**
     * Return the value of the key If this key has no value, returns -1
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        try {
            String sKey = ResourceBundle.getBundle(BUNDLE_NAME).getString(key);
            return new Integer(sKey).intValue();
        } catch (MissingResourceException e) {
            return -1;
        }

    }
}
