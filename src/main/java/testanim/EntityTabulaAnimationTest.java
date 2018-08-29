package testanim;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.animation.NamedAnimation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * @author jglrxavpok
 */
public class EntityTabulaAnimationTest extends EntityLiving implements IAnimatedEntity {
    private int animationTick;
    private Animation currentAnimation = IAnimatedEntity.NO_ANIMATION;

    // the names used must be the **exact** same than in Tabula
    private Animation TEST_ANIMATION = NamedAnimation.create("Looping animation", 20).setLooping(true);
    private Animation TEST_ANIMATION2 = NamedAnimation.create("Non-looping animation", 20);//.setLooping(false);

    public EntityTabulaAnimationTest(World worldIn) {
        super(worldIn);
        setSize(1f, 1f);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        setAnimationTick(0);
        if(player.isSneaking()) {
            setAnimation(TEST_ANIMATION2);
        } else {
            setAnimation(TEST_ANIMATION);
        }
        player.sendStatusMessage(new TextComponentString("Now playing animation "+((NamedAnimation)getAnimation()).getName()), true);
        return true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0);
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { NO_ANIMATION, TEST_ANIMATION, TEST_ANIMATION2 };
    }
}
