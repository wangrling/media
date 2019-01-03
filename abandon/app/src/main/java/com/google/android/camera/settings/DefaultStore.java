package com.google.android.camera.settings;

/**
 * A class for storing default values and possible values of SharedPreferences settings. It is
 * optional to store defaults and possible values for a setting. If a default is not specified,
 * the SettingsManager API chooses a default based on the type requested:
 *
 * <ul>getString: default is null</ul>
 * <ul>getInteger: default is 0</ul>
 * <ul>getBoolean: default is false</ul>
 *
 * If possible values aren't specified for a SharedPreferences key, then calling
 * getIndexCurrentValue and setValueByIndex will throw an IllegalArgumentException.
 */

public class DefaultStore {

    /**
     * A class for storing a default value and set of possible
     * values.  Since all settings values are saved as Strings in
     * SharedPreferences, the default and possible values are
     * Strings.  This simplifies default values management.
     */
    private static class Defaults {
        private String mDefaultValue;
        private String[] mPossibleValues;

        public Defaults(String defaultValue, String[] possibleValues) {
            mDefaultValue = defaultValue;
            mPossibleValues = possibleValues;
        }

        public String getDefaultValue() {
            return mDefaultValue;
        }

        public String[] getPossibleValues() {
            return mPossibleValues;
        }
    }

    /** Map f Defaults for SharedPreferences keys. */
}
