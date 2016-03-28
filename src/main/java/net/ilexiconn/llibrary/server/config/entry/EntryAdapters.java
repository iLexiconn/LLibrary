package net.ilexiconn.llibrary.server.config.entry;

import net.ilexiconn.llibrary.server.config.ConfigEntry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public enum EntryAdapters implements IEntryAdapter {
    INTEGER {
        @Override
        public Object getValue(Configuration config, String name, ConfigEntry entry, Object defaultValue) {
            int minInt = entry.minValue().isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(entry.minValue());
            int maxInt = entry.maxValue().isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(entry.maxValue());
            return config.getInt(name, entry.category(), (int) defaultValue, minInt, maxInt, entry.comment());
        }
    },

    BOOLEAN {
        @Override
        public Object getValue(Configuration config, String name, ConfigEntry entry, Object defaultValue) {
            return config.getBoolean(name, entry.category(), (boolean) defaultValue, entry.comment());
        }
    },

    STRING {
        @Override
        public Object getValue(Configuration config, String name, ConfigEntry entry, Object defaultValue) {
            return config.getString(name, entry.category(), (String) defaultValue, entry.comment());
        }
    },

    FLOAT {
        @Override
        public Object getValue(Configuration config, String name, ConfigEntry entry, Object defaultValue) {
            float minFloat = entry.minValue().isEmpty() ? Float.MIN_VALUE : Float.parseFloat(entry.minValue());
            float maxFloat = entry.maxValue().isEmpty() ? Float.MAX_VALUE : Float.parseFloat(entry.maxValue());
            return config.getFloat(name, entry.category(), (float) defaultValue, minFloat, maxFloat, entry.comment());
        }
    },

    DOUBLE {
        @Override
        public Object getValue(Configuration config, String name, ConfigEntry entry, Object defaultValue) {
            double minDouble = entry.minValue().isEmpty() ? Double.MIN_VALUE : Double.parseDouble(entry.minValue());
            double maxDouble = entry.maxValue().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(entry.maxValue());
            Property property = config.get(entry.category(), name, Double.toString((double) defaultValue), name);
            property.setComment(entry.comment() + " [range: " + minDouble + " ~ " + maxDouble + ", default: " + defaultValue + "]");;
            property.setMinValue(minDouble);
            property.setMaxValue(maxDouble);
            try {
                return Double.parseDouble(property.getString()) < minDouble ? minDouble : (Double.parseDouble(property.getString()) > maxDouble ? maxDouble : Double.parseDouble(property.getString()));
            } catch (Exception e) {
                e.printStackTrace();
                return defaultValue;
            }
        }
    },

    INT_ARRAY {
        @Override
        public Object getValue(Configuration config, String name, ConfigEntry entry, Object defaultValue) {
            Property property = config.get(entry.category(), name, (int[]) defaultValue);
            property.setValidValues(entry.validValues());
            property.setComment(entry.comment() + " [default: " + property.getDefault() + "]");
            return property.getIntList();
        }
    },

    BOOLEAN_ARRAY {
        @Override
        public Object getValue(Configuration config, String name, ConfigEntry entry, Object defaultValue) {
            Property property = config.get(entry.category(), name, (boolean[]) defaultValue);
            property.setValidValues(entry.validValues());
            property.setComment(entry.comment() + " [default: " + property.getDefault() + "]");
            return property.getBooleanList();
        }
    },

    STRING_ARRAY {
        @Override
        public Object getValue(Configuration config, String name, ConfigEntry entry, Object defaultValue) {
            return config.getStringList(name, entry.category(), (String[]) defaultValue, entry.comment());
        }
    },

    FLOAT_ARRAY {
        @Override
        public Object getValue(Configuration config, String name, ConfigEntry entry, Object defaultValue) {
            float[] floatArray = (float[]) defaultValue;
            double[] doubleArray = new double[floatArray.length];
            for (int i = 0; i < doubleArray.length; i++) {
                doubleArray[i] = (double) floatArray[i];
            }
            Property property = config.get(entry.category(), name, doubleArray);
            property.setValidValues(entry.validValues());
            property.setComment(entry.comment() + " [default: " + property.getDefault() + "]");
            doubleArray = property.getDoubleList();
            floatArray = new float[doubleArray.length];
            for (int i = 0; i < doubleArray.length; i++) {
                floatArray[i] = (float) doubleArray[i];
            }
            return floatArray;
        }
    },

    DOUBLE_ARRAY {
        @Override
        public Object getValue(Configuration config, String name, ConfigEntry entry, Object defaultValue) {
            Property property = config.get(entry.category(), name, (double[]) defaultValue);
            property.setValidValues(entry.validValues());
            property.setComment(entry.comment() + " [default: " + property.getDefault() + "]");
            return property.getDoubleList();
        }
    };

    public static <T> IEntryAdapter<T> getBuiltinAdapter(Class<T> type) {
        if (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)) {
            return INTEGER;
        } else if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
            return BOOLEAN;
        } else if (String.class.isAssignableFrom(type)) {
            return STRING;
        } else if (Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type)) {
            return FLOAT;
        } else if (Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)) {
            return DOUBLE;
        } else if (Integer[].class.isAssignableFrom(type) || int[].class.isAssignableFrom(type)) {
            return INT_ARRAY;
        } else if (Boolean[].class.isAssignableFrom(type) || boolean[].class.isAssignableFrom(type)) {
            return BOOLEAN_ARRAY;
        } else if (String[].class.isAssignableFrom(type)) {
            return STRING_ARRAY;
        } else if (Float[].class.isAssignableFrom(type) || float[].class.isAssignableFrom(type)) {
            return FLOAT_ARRAY;
        } else if (Double[].class.isAssignableFrom(type) || double[].class.isAssignableFrom(type)) {
            return DOUBLE_ARRAY;
        } else {
            return null;
        }
    }
}
