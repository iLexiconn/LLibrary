package net.ilexiconn.llibrary.server.nbt;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.nbt.parser.INBTParser;
import net.ilexiconn.llibrary.server.nbt.parser.NBTParsers;
import net.minecraft.crash.CrashReport;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pau101
 * @since 1.1.0
 */
public enum NBTHandler {
    INSTANCE;

    private Map<Class<?>, INBTParser<?>> nbtParserMap = new HashMap<>();

    public <T> void registerNBTParser(Class<T> type, INBTParser<T> nbtParser) {
        this.nbtParserMap.put(type, nbtParser);
    }

    public void loadNBTData(Object object, NBTTagCompound compound) {
        Class<?> clazz = object.getClass();
        do {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(NBTProperty.class)) {
                    field.setAccessible(true);
                    String name = field.getAnnotation(NBTProperty.class).name();
                    if (name.isEmpty()) {
                        name = field.getName();
                    }
                    readFromNBTToField(object, field, name, compound);
                } else if (field.isAnnotationPresent(NBTMutatorProperty.class)) {
                    NBTMutatorProperty mutatorProperty = field.getAnnotation(NBTMutatorProperty.class);
                    String name = mutatorProperty.name();
                    if (name.isEmpty()) {
                        name = field.getName();
                    }
                    Class<?> type = mutatorProperty.type();
                    Method setter = getSetter(clazz, name, type, mutatorProperty.setter());
                    readFromNBTToSetter(object, setter, name, compound);
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
    }

    public void saveNBTData(Object object, NBTTagCompound compound) {
        Class<?> clazz = object.getClass();
        do {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(NBTProperty.class)) {
                    field.setAccessible(true);
                    String name = field.getAnnotation(NBTProperty.class).name();
                    if (name.isEmpty()) {
                        name = field.getName();
                    }
                    NBTBase tag = writeFieldToNBT(object, field);
                    if (tag != null) {
                        compound.setTag(name, tag);
                    }
                } else if (field.isAnnotationPresent(NBTMutatorProperty.class)) {
                    NBTMutatorProperty mutatorProperty = field.getAnnotation(NBTMutatorProperty.class);
                    String name = mutatorProperty.name();
                    if (name.isEmpty()) {
                        name = field.getName();
                    }
                    Class<?> type = mutatorProperty.type();
                    Method getter = getGetter(clazz, name, type, mutatorProperty.getter());
                    NBTBase tag = writeGetterValueToNBT(object, getter);
                    if (tag != null) {
                        compound.setTag(name, tag);
                    }
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
    }

    public <T> INBTParser<T> getParserForType(Class<T> type) {
        INBTParser<T> nbtParser = NBTParsers.getBuiltinParser(type);
        if (nbtParser != null) {
            return nbtParser;
        } else if (this.nbtParserMap.containsKey(type)) {
            return (INBTParser<T>) this.nbtParserMap.get(type);
        } else {
            return null;
        }
    }

    private <T> NBTBase writeFieldToNBT(T object, Field field) {
        T value;
        try {
            value = (T) field.get(object);
        } catch (Exception e) {
            LLibrary.LOGGER.fatal(CrashReport.makeCrashReport(e.getCause(), e.getCause().getLocalizedMessage()).getCompleteReport());
            return null;
        }
        return this.writeToNBT((Class<T>) field.getType(), value);
    }

    private <T> NBTBase writeGetterValueToNBT(T object, Method getter) {
        T value;
        try {
            value = (T) getter.invoke(object);
        } catch (Exception e) {
            LLibrary.LOGGER.fatal(CrashReport.makeCrashReport(e.getCause(), e.getCause().getLocalizedMessage()).getCompleteReport());
            return null;
        }
        return this.writeToNBT((Class<T>) getter.getReturnType(), value);
    }

    private <T> NBTBase writeToNBT(Class<T> type, T value) {
        if (value == null) {
            return null;
        }
        INBTParser<T> nbtParser = this.getParserForType(type);
        if (nbtParser != null) {
            return nbtParser.parseValue(value);
        } else {
            NBTTagCompound compound = new NBTTagCompound();
            this.saveNBTData(value, compound);
            return compound;
        }
    }

    private void readFromNBTToField(Object object, Field field, String name, NBTTagCompound compound) {
        NBTBase valueNBT = compound.getTag(name);
        if (valueNBT == null) {
            return;
        }
        Object value = readFromNBT(field.getType(), valueNBT);
        try {
            field.set(object, value);
        } catch (Exception e) {
            LLibrary.LOGGER.fatal(CrashReport.makeCrashReport(e.getCause(), e.getCause().getLocalizedMessage()).getCompleteReport());
        }
    }

    private void readFromNBTToSetter(Object object, Method setter, String name, NBTTagCompound compound) {
        NBTBase valueNBT = compound.getTag(name);
        if (valueNBT == null) {
            return;
        }
        Object value = readFromNBT(setter.getParameters()[0].getType(), valueNBT);
        try {
            setter.invoke(object, value);
        } catch (Exception e) {
            LLibrary.LOGGER.fatal(CrashReport.makeCrashReport(e.getCause(), e.getCause().getLocalizedMessage()).getCompleteReport());
        }
    }

    private <T> T readFromNBT(Class<T> type, NBTBase tag) {
        INBTParser<T> nbtParser = this.getParserForType(type);
        if (nbtParser != null) {
            return nbtParser.parseTag(tag);
        } else {
            return null;
        }
    }

    private Method getSetter(Class<?> clazz, String name, Class<?> type, String setter) {
        String setterName = getConventionalSetterName(name, setter);
        return resolveMutator(clazz, setterName, type);
    }

    private Method getGetter(Class<?> clazz, String name, Class<?> type, String getter) {
        String getterName = getConventionalGetterName(name, type, getter);
        return resolveMutator(clazz, getterName);
    }

    private Method resolveMutator(Class<?> clazz, String name, Class<?>... param) {
        Method method = null;
        Class<?> methodClass = clazz;
        do {
            try {
                method = methodClass.getDeclaredMethod(name, param);
                break;
            } catch (NoSuchMethodException ignored) {

            }
        } while ((methodClass = methodClass.getSuperclass()) != null);
        if (method == null) {
            String message = clazz.getName() + "." + name + "(" + (param.length == 0 ? "" : param[0] == null ? "null" : param[0].getName()) + ")";
            LLibrary.LOGGER.fatal(CrashReport.makeCrashReport(new RuntimeException(message), message));
            return null;
        }
        method.setAccessible(true);
        return method;
    }

    private String getConventionalSetterName(String name, String setter) {
        return getConventionalMutatorName("set", name, setter);
    }

    private String getConventionalGetterName(String name, Class<?> type, String getter) {
        return getConventionalMutatorName(type == Boolean.class || type == boolean.class ? "is" : "get", name, getter);
    }

    private String getConventionalMutatorName(String verb, String name, String mutator) {
        if (mutator.isEmpty()) {
            return verb + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }
        return mutator;
    }
}
