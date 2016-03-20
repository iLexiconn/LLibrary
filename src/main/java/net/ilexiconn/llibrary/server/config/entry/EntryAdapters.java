package net.ilexiconn.llibrary.server.config.entry;

import net.ilexiconn.llibrary.server.config.ConfigEntry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.lang.reflect.Field;

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
            property.comment = entry.comment() + " [range: " + minDouble + " ~ " + maxDouble + ", default: " + defaultValue + "]";
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
            property.comment = entry.comment() + " [default: " + property.getDefault() + "]";
            return property.getIntList();
        }
    },

    BOOLEAN_ARRAY {
        @Override
        public Object getValue(Configuration config, String name, ConfigEntry entry, Object defaultValue) {
            Property property = config.get(entry.category(), name, (boolean[]) defaultValue);
            property.setValidValues(entry.validValues());
            property.comment = entry.comment() + " [default: " + property.getDefault() + "]";
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
            property.comment = entry.comment() + " [default: " + property.getDefault() + "]";
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
            property.comment = entry.comment() + " [default: " + property.getDefault() + "]";
            return property.getDoubleList();
        }
    };

    public static IEntryAdapter getBuiltinAdaper(Field field) {
        if (Integer.class.isAssignableFrom(field.getType()) || int.class.isAssignableFrom(field.getType())) {
            return INTEGER;
        } else if (Boolean.class.isAssignableFrom(field.getType()) || boolean.class.isAssignableFrom(field.getType())) {
            return BOOLEAN;
        } else if (String.class.isAssignableFrom(field.getType())) {
            return STRING;
        } else if (Float.class.isAssignableFrom(field.getType()) || float.class.isAssignableFrom(field.getType())) {
            return FLOAT;
        } else if (Double.class.isAssignableFrom(field.getType()) || double.class.isAssignableFrom(field.getType())) {
            return DOUBLE;
        } else if (Integer[].class.isAssignableFrom(field.getType()) || int[].class.isAssignableFrom(field.getType())) {
            return INT_ARRAY;
        } else if (Boolean[].class.isAssignableFrom(field.getType()) || boolean[].class.isAssignableFrom(field.getType())) {
            return BOOLEAN_ARRAY;
        } else if (String[].class.isAssignableFrom(field.getType())) {
            return STRING_ARRAY;
        } else if (Float[].class.isAssignableFrom(field.getType()) || float[].class.isAssignableFrom(field.getType())) {
            return FLOAT_ARRAY;
        } else if (Double[].class.isAssignableFrom(field.getType()) || double[].class.isAssignableFrom(field.getType())) {
            return DOUBLE_ARRAY;
        } else {
            return null;
        }
    }
}
