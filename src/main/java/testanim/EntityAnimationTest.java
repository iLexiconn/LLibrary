package testanim;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.animation.NamedAnimation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityAnimationTest extends EntityLiving implements IAnimatedEntity {
    private int animationTick;
    private Animation currentAnimation = IAnimatedEntity.NO_ANIMATION;
    private Animation TEST_ANIMATION = NamedAnimation.create("Looping animation", 20).setLooping(true);

    public EntityAnimationTest(World worldIn) {
        super(worldIn);
        setSize(1f, 1f);
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
        if(currentAnimation == NO_ANIMATION) {
            setAnimation(TEST_ANIMATION);
        }
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
        return new Animation[] { NO_ANIMATION, TEST_ANIMATION };
    }
}
