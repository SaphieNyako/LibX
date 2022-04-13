package io.github.noeppi_noeppi.libx.crafting.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.noeppi_noeppi.libx.annotation.meta.RemoveIn;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.NBTIngredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

// TODO rename in 1.19 and use share tag by default because ingredients are
//  synced with the client.
/**
 * An ingredient that unlike the one by forge does not check the share tag but the actual
 * ItemStack tag.
 */
public class NbtIngredient extends Ingredient {

    private final ItemStack stack;
    private final ItemStack[] matchingStacks;

    /**
     * If this is true, the tags must be equals. If it is false the matched item must at
     * least have all the tags from this ingredient but may specify more.
     */
    public final boolean exactMatch;

    public NbtIngredient(ItemStack stack) {
        this(stack, false);
    }

    /**
     * @deprecated Use {@link NBTIngredient} for exact matches
     */
    @Deprecated(forRemoval = true)
    @RemoveIn(minecraft = "1.19")
    public NbtIngredient(ItemStack stack, boolean exactMatch) {
        super(Stream.of(new Ingredient.ItemValue(stack)));
        this.stack = stack.copy();
        this.exactMatch = exactMatch;
        this.matchingStacks = new ItemStack[] { stack.copy() };
    }

    @Override
    public boolean test(@Nullable ItemStack input) {
        if (input == null || this.stack.isEmpty() || input.getItem() != this.stack.getItem())
            return false;
        if (input.isDamageableItem() != this.stack.isDamageableItem() || (this.stack.isDamageableItem() && input.getDamageValue() != this.stack.getDamageValue())) {
            return false;
        }

        CompoundTag nbt = this.stack.getTag();
        if (nbt == null) {
            nbt = new CompoundTag();
        }

        CompoundTag inputNbt = input.getTag();
        if (inputNbt == null) {
            inputNbt = new CompoundTag();
        }

        if (this.exactMatch) {
            return inputNbt.equals(nbt);
        } else {
            CompoundTag merged = inputNbt.copy();
            merged = merged.merge(nbt);

            return merged.equals(inputNbt);
        }
    }

    @Nonnull
    @Override
    public ItemStack[] getItems() {
        return this.matchingStacks;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Nonnull
    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return NbtIngredient.Serializer.INSTANCE;
    }

    @Nonnull
    @Override
    @SuppressWarnings("ConstantConditions")
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(NbtIngredient.Serializer.INSTANCE).toString());
        json.addProperty("item", this.stack.getItem().getRegistryName().toString());
        if (this.stack.hasTag() && !this.stack.getOrCreateTag().isEmpty()) {
            json.addProperty("nbt", this.stack.getTag().toString());
        }
        json.addProperty("exactMatch", this.exactMatch);
        return json;
    }

    public static class Serializer implements IIngredientSerializer<NbtIngredient> {

        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {

        }

        @Nonnull
        @Override
        public NbtIngredient parse(FriendlyByteBuf buffer) {
            return new NbtIngredient(buffer.readItem(), buffer.readBoolean());
        }

        @Nonnull
        @Override
        public NbtIngredient parse(@Nonnull JsonObject json) {
            ItemStack stack = CraftingHelper.getItemStack(json, true);
            return new NbtIngredient(stack, json.has("exactMatch") && json.get("exactMatch").getAsBoolean());
        }

        @Override
        public void write(FriendlyByteBuf buffer, NbtIngredient ingredient) {
            buffer.writeItem(ingredient.stack);
            buffer.writeBoolean(ingredient.exactMatch);
        }
    }
}
