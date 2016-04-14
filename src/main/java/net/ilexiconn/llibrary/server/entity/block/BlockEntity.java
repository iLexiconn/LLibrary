package net.ilexiconn.llibrary.server.entity.block;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.network.BlockEntityMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

/**
 * @author iLexiconn
 * @since 1.3.0
 */
public abstract class BlockEntity extends TileEntity implements ITickable {
    private NBTTagCompound lastCompound;
    private int trackingUpdateTimer = 0;

    @Override
    public final void update() {
        int trackingUpdateFrequency = this.getTrackingUpdateTime();
        if (this.trackingUpdateTimer < trackingUpdateFrequency) {
            this.trackingUpdateTimer++;
        }
        if (this.trackingUpdateTimer >= trackingUpdateFrequency) {
            this.trackingUpdateTimer = 0;
            NBTTagCompound compound = new NBTTagCompound();
            this.saveTrackingSensitiveData(compound);
            if (!compound.equals(this.lastCompound)) {
                if (!this.worldObj.isRemote) {
                    this.onSync();
                    LLibrary.NETWORK_WRAPPER.sendToAll(new BlockEntityMessage(this));
                }
                this.lastCompound = compound;
            }
        }
        this.onUpdate();
    }

    @Override
    public final Packet<?> getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        this.writeToNBT(compound);
        return new S35PacketUpdateTileEntity(this.pos, 0, compound);
    }

    @Override
    public final void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public final void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.loadNBTData(compound);
    }

    @Override
    public final void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.saveNBTData(compound);
    }

    /**
     * Write any tracking sensitive data to this NBT. The tracker will fire if
     * the NBT isn't equal and the tracking timer is ready.
     *
     * @param compound the compound to save to
     */
    public void saveTrackingSensitiveData(NBTTagCompound compound) {
        this.saveNBTData(compound);
    }

    /**
     * Client reads tracking sensitive data from this hook
     *
     * @param compound the compound to load from
     */
    public void loadTrackingSensitiveData(NBTTagCompound compound) {
        this.loadNBTData(compound);
    }

    /**
     * Save all needed data to the tag.
     *
     * @param compound the compound to save to
     */
    public abstract void saveNBTData(NBTTagCompound compound);

    /**
     * Load all needed data from the tag
     *
     * @param compound the compound to load from
     */
    public abstract void loadNBTData(NBTTagCompound compound);

    /**
     * Called every tick
     */
    public void onUpdate() {

    }

    /**
     * Called when the data is syncing
     */
    public void onSync() {

    }

    /**
     * @return how often the tracking sensitive data is compared
     */
    public int getTrackingUpdateTime() {
        return 0;
    }
}
