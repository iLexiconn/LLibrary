package net.ilexiconn.llibrary.server.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.server.structure.rule.PlaceRule;
import net.ilexiconn.llibrary.server.structure.rule.RepeatRule;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.List;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public class ComponentInfo {
    public HashMap<BlockCoords, BlockList> blocks;
    public List<RepeatRule> repeats;
    public EnumFacing facing;

    public ComponentInfo() {
        facing = EnumFacing.NORTH;
        blocks = Maps.newHashMap();
        repeats = Lists.newArrayList();
        repeats.add(new PlaceRule());
    }
}
