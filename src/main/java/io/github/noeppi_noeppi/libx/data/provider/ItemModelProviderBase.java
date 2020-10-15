package io.github.noeppi_noeppi.libx.data.provider;

import io.github.noeppi_noeppi.libx.data.AlwaysExistentModelFile;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * A base class for item model provider. When overriding this you should call the {@code handheld} and
 * {@code manualModel} methods in constructor.
 */
public class ItemModelProviderBase extends ItemModelProvider {

    public static final ResourceLocation GENERATED = new ResourceLocation("item/generated");
    public static final ResourceLocation HANDHELD = new ResourceLocation("item/handheld");

    protected final ModX mod;

    private final Set<Item> handheld = new HashSet<>();
    private final Set<Item> blacklist = new HashSet<>();

    public ItemModelProviderBase(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, mod.modid, fileHelper);
        this.mod = mod;
    }

    @Nonnull
    @Override
    public final String getName() {
        return this.mod.modid + " item models";
    }

    /**
     * This item will get a handheld model.
     */
    protected void handheld(Item item) {
        this.handheld.add(item);
    }

    /**
     * This item will not be processed by the generator.
     */
    protected void manualModel(Item item) {
        this.blacklist.add(item);
    }

    @Override
    protected void registerModels() {
        for (ResourceLocation id : ForgeRegistries.ITEMS.getKeys()) {
            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (item != null && this.mod.modid.equals(id.getNamespace()) && !this.blacklist.contains(item)) {
                if (item instanceof BlockItem) {
                    this.defaultBlock(id, (BlockItem) item);
                } else if (this.handheld.contains(item)) {
                    this.withExistingParent(id.getPath(), HANDHELD).texture("layer0", new ResourceLocation(id.getNamespace(), "item/" + id.getPath()));
                } else {
                    this.defaultItem(id, item);
                }
            }
        }
    }

    protected void defaultItem(ResourceLocation id, Item item) {
        this.withExistingParent(id.getPath(), GENERATED).texture("layer0", new ResourceLocation(id.getNamespace(), "item/" + id.getPath()));
    }

    protected void defaultBlock(ResourceLocation id, BlockItem item) {
        this.getBuilder(id.getPath()).parent(new AlwaysExistentModelFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath())));
    }
}