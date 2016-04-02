package net.ilexiconn.llibrary.server.structure;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public class BlockCoords {
    public int x;
    public int y;
    public int z;

    public BlockCoords() {

    }

    public BlockCoords(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Object o) {
        if (o instanceof BlockCoords) {
            BlockCoords coords = (BlockCoords) o;
            return coords.x == x && coords.y == y && coords.z == z;
        }
        return false;
    }

    public int hashCode() {
        final int BASE = 17;
        final int MULTIPLIER = 31;

        int result = BASE;
        result = MULTIPLIER * result + x;
        result = MULTIPLIER * result + y;
        result = MULTIPLIER * result + z;
        return result;
    }
}
