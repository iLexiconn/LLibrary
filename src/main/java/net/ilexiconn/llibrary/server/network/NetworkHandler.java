package net.ilexiconn.llibrary.server.network;

import com.google.common.collect.SetMultimap;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.ilexiconn.llibrary.LLibrary;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Handler class for message IDs.
 *
 * @author iLexiconn
 * @since 1.2.0
 */
public enum NetworkHandler {
    INSTANCE;

    private Map<SimpleNetworkWrapper, Integer> idMap = new HashMap<>();

    /**
     * Register a message to both sides.
     *
     * @param networkWrapper the network wrapper
     * @param clazz          the message class
     * @param <T>            the message type
     */
    public <T extends AbstractMessage<T> & IMessageHandler<T, IMessage>> void registerMessage(SimpleNetworkWrapper networkWrapper, Class<T> clazz) {
        try {
            AbstractMessage<T> message = clazz.getDeclaredConstructor().newInstance();
            if (message.registerOnSide(Side.CLIENT)) {
                this.registerMessage(networkWrapper, clazz, Side.CLIENT);
            }
            if (message.registerOnSide(Side.SERVER)) {
                this.registerMessage(networkWrapper, clazz, Side.SERVER);
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register a message to a specific side.
     *
     * @param networkWrapper the network wrapper
     * @param clazz          the message class
     * @param side           the side
     * @param <T>            the message type
     */
    public <T extends AbstractMessage<T> & IMessageHandler<T, IMessage>> void registerMessage(SimpleNetworkWrapper networkWrapper, Class<T> clazz, Side side) {
        int id = 0;
        if (this.idMap.containsKey(networkWrapper)) {
            id = this.idMap.get(networkWrapper);
        }
        networkWrapper.registerMessage(clazz, clazz, id, side);
        this.idMap.put(networkWrapper, id + 1);
    }

    public void injectNetworkWrapper(ModContainer mod, ASMDataTable data) {
        SetMultimap<String, ASMDataTable.ASMData> annotations = data.getAnnotationsFor(mod);
        if (annotations != null) {
            Set<ASMDataTable.ASMData> targetList = annotations.get(NetworkWrapper.class.getName());
            ClassLoader classLoader = Loader.instance().getModClassLoader();
            for (ASMDataTable.ASMData target : targetList) {
                try {
                    Class<?> targetClass = Class.forName(target.getClassName(), true, classLoader);
                    Field field = targetClass.getDeclaredField(target.getObjectName());
                    field.setAccessible(true);
                    NetworkWrapper annotation = field.getAnnotation(NetworkWrapper.class);
                    field.set(mod.getMod(), NetworkRegistry.INSTANCE.newSimpleChannel(mod.getModId()));
                    SimpleNetworkWrapper networkWrapper = (SimpleNetworkWrapper) field.get(mod.getMod());
                    for (Class messageClass : annotation.value()) {
                        registerMessage(networkWrapper, messageClass);
                    }
                } catch (Exception e) {
                    LLibrary.LOGGER.fatal("Failed to inject network wrapper for mod container " + mod, e);
                }
            }
        }
    }
}
