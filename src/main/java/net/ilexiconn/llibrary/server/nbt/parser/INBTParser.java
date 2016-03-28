package net.ilexiconn.llibrary.server.nbt.parser;

import net.minecraft.nbt.NBTBase;

/**
 * @author iLexiconn
 * @since 1.1.0
 */
public interface INBTParser<T> {
    T parseTag(NBTBase tag);

    NBTBase parseValue(T value);
}
