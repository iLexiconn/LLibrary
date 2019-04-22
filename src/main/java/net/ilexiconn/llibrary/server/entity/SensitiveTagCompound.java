package net.ilexiconn.llibrary.server.entity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

public class SensitiveTagCompound extends NBTTagCompound {

	private boolean changed = false;

	@Override
	public void setByte(String key, byte value) {
		NBTBase b = this.tagMap.put(key, new NBTTagByte(value));
		if (b == null) changed = true;
		else if (b instanceof NBTTagByte && ((NBTTagByte) b).getByte() != value) changed = true;
	}

	@Override
	public void setByteArray(String key, byte[] value) {
		NBTBase b = this.tagMap.put(key, new NBTTagByteArray(value));
		if (b == null) changed = true;
		else if (b instanceof NBTTagByteArray && ((NBTTagByteArray) b).getByteArray() != value) changed = true;
	}

	@Override
	public void setDouble(String key, double value) {
		NBTBase b = this.tagMap.put(key, new NBTTagDouble(value));
		if (b == null) changed = true;
		else if (b instanceof NBTTagDouble && ((NBTTagDouble) b).getDouble() != value) changed = true;
	}

	@Override
	public void setFloat(String key, float value) {
		NBTBase b = this.tagMap.put(key, new NBTTagFloat(value));
		if (b == null) changed = true;
		else if (b instanceof NBTTagFloat && ((NBTTagFloat) b).getFloat() != value) changed = true;
	}

	@Override
	public void setIntArray(String key, int[] value) {
		NBTBase b = this.tagMap.put(key, new NBTTagIntArray(value));
		if (b == null) changed = true;
		else if (b instanceof NBTTagIntArray && ((NBTTagIntArray) b).getIntArray() != value) changed = true;
	}

	@Override
	public void setInteger(String key, int value) {
		NBTBase b = this.tagMap.put(key, new NBTTagInt(value));
		if (b == null) changed = true;
		else if (b instanceof NBTTagInt && ((NBTTagInt) b).getInt() != value) changed = true;
	}

	@Override
	public void setLong(String key, long value) {
		NBTBase b = this.tagMap.put(key, new NBTTagLong(value));
		if (b == null) changed = true;
		else if (b instanceof NBTTagLong && ((NBTTagLong) b).getLong() != value) changed = true;
	}

	@Override
	public void setShort(String key, short value) {
		NBTBase b = this.tagMap.put(key, new NBTTagShort(value));
		if (b == null) changed = true;
		else if (b instanceof NBTTagShort && ((NBTTagShort) b).getShort() != value) changed = true;
	}

	@Override
	public void setString(String key, String value) {
		NBTBase b = this.tagMap.put(key, new NBTTagString(value));
		if (b == null) changed = true;
		else if (b instanceof NBTTagString && !((NBTTagString) b).getString().equals(value)) changed = true;
	}

	@Override
	public void setTag(String key, NBTBase value) {
		NBTBase b = this.tagMap.put(key, value);
		if (b == null) changed = true;
		else if (!b.equals(value)) changed = true;
	}

	public boolean hasChanged() {
		return changed;
	}

	public void reset() {
		changed = false;
	}

}
