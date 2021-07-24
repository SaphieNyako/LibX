package io.github.noeppi_noeppi.libx.crafting;

import com.google.gson.JsonObject;
import io.github.noeppi_noeppi.libx.util.NbtToJson;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;

/**
 * Contains some methods that are missing in Forges' {@link CraftingHelper}
 */
// TODO do we still need this?
public class CraftingHelper2 {

    /**
     * Writes an {@link ItemStack} the way it's expected in a recipe json.
     *
     * @param writeNbt Whether the stacks nbt tag should be written.
     */
    public static JsonObject serializeItemStack(ItemStack stack, boolean writeNbt) {
        JsonObject json = new JsonObject();
        //noinspection ConstantConditions
        json.addProperty("item", stack.getItem().getRegistryName().toString());

        if (stack.getCount() != 1) {
            json.addProperty("count", stack.getCount());
        }
        if (writeNbt && stack.hasTag()) {
            json.add("nbt", NbtToJson.getJson(stack.getOrCreateTag(), false));
        }
        return json;
    }
}
