package net.ilexiconn.llibrary.client.model.tabula.baked.deserializer;

import com.google.gson.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Type;

/**
 * @author pau101
 * @since 1.0.0
 */
@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class ItemCameraTransformsDeserializer implements JsonDeserializer<ItemCameraTransforms> {
    public ItemCameraTransforms deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();
        ItemTransformVec3f thirdperson = this.getTransform(context, object, "thirdperson");
        ItemTransformVec3f firstperson = this.getTransform(context, object, "firstperson");
        ItemTransformVec3f head = this.getTransform(context, object, "head");
        ItemTransformVec3f gui = this.getTransform(context, object, "gui");
        ItemTransformVec3f ground = this.getTransform(context, object, "ground");
        ItemTransformVec3f fixed = this.getTransform(context, object, "fixed");
        return new ItemCameraTransforms(thirdperson, firstperson, head, gui, ground, fixed);
    }

    private ItemTransformVec3f getTransform(JsonDeserializationContext context, JsonObject object, String key) {
        return object.has(key) ? (ItemTransformVec3f) context.deserialize(object.get(key), ItemTransformVec3f.class) : ItemTransformVec3f.DEFAULT;
    }
}

