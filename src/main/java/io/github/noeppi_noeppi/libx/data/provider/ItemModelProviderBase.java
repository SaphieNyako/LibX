package io.github.noeppi_noeppi.libx.data.provider;

import io.github.noeppi_noeppi.libx.LibX;
import io.github.noeppi_noeppi.libx.data.AlwaysExistentModelFile;
import io.github.noeppi_noeppi.libx.impl.RendererOnDataGenException;
import io.github.noeppi_noeppi.libx.impl.base.decoration.blocks.*;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.client.RenderProperties;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A base class for item model provider. An extending class should call the
 * {@link #handheld(Item) handheld} and {@link #manualModel(Item) manualModel} methods
 * in {@link #setup() setup}.
 */
public abstract class ItemModelProviderBase extends ItemModelProvider {

    public static final ResourceLocation GENERATED = new ResourceLocation("item/generated");
    public static final ResourceLocation HANDHELD = new ResourceLocation("item/handheld");
    public static final ResourceLocation DRIPPING_BUCKET = new ResourceLocation("forge", "bucket_drip");
    public static final ResourceLocation SPECIAL_BLOCK_PARENT = new ResourceLocation(LibX.getInstance().modid, "item/base/special_block");
    public static final ResourceLocation SPAWN_EGG_PARENT = new ResourceLocation("minecraft", "item/template_spawn_egg");
    public static final ResourceLocation FENCE_PARENT = new ResourceLocation("minecraft", "block/fence_inventory");
    public static final ResourceLocation BUTTON_PARENT = new ResourceLocation("minecraft", "block/button_inventory");

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
        this.setup();

        for (ResourceLocation id : ForgeRegistries.ITEMS.getKeys()) {
            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (item != null && this.mod.modid.equals(id.getNamespace()) && !this.blacklist.contains(item)) {
                if (item instanceof BlockItem blockItem) {
                    this.defaultBlock(id, blockItem);
                } else if (this.handheld.contains(item)) {
                    this.withExistingParent(id.getPath(), HANDHELD).texture("layer0", new ResourceLocation(id.getNamespace(), "item/" + id.getPath()));
                } else {
                    this.defaultItem(id, item);
                }
            }
        }
    }

    protected abstract void setup();

    protected void defaultItem(ResourceLocation id, Item item) {
        if (item instanceof SpawnEggItem) {
            this.withExistingParent(id.getPath(), SPAWN_EGG_PARENT);
        } else if (item instanceof BucketItem bucketItem) {
            this.withExistingParent(id.getPath(), DRIPPING_BUCKET)
                    .texture("base", this.modLoc("item/" + id.getPath()))
                    .customLoader(DynamicBucketModelBuilder::begin)
                    .fluid(bucketItem.getFluid());
        } else {
            this.withExistingParent(id.getPath(), GENERATED).texture("layer0", new ResourceLocation(id.getNamespace(), "item/" + id.getPath()));
        }
    }

    protected void defaultBlock(ResourceLocation id, BlockItem item) {
        if (isItemStackRenderer(RenderProperties.get(item))) {
            this.getBuilder(id.getPath()).parent(new AlwaysExistentModelFile(SPECIAL_BLOCK_PARENT));
        } else if (item.getBlock() instanceof DecoratedFenceBlock decorated) {
            ResourceLocation parentId = Objects.requireNonNull(decorated.parent.getRegistryName());
            ResourceLocation texture = new ResourceLocation(parentId.getNamespace(), "block/" + parentId.getPath());
            this.getBuilder(id.getPath()).parent(new AlwaysExistentModelFile(FENCE_PARENT)).texture("texture", texture);
        } else if (item.getBlock() instanceof DecoratedWoodButton decorated) {
            ResourceLocation parentId = Objects.requireNonNull(decorated.parent.getRegistryName());
            ResourceLocation texture = new ResourceLocation(parentId.getNamespace(), "block/" + parentId.getPath());
            this.getBuilder(id.getPath()).parent(new AlwaysExistentModelFile(BUTTON_PARENT)).texture("texture", texture);
        } else if (item.getBlock() instanceof DecoratedStoneButton decorated) {
            ResourceLocation parentId = Objects.requireNonNull(decorated.parent.getRegistryName());
            ResourceLocation texture = new ResourceLocation(parentId.getNamespace(), "block/" + parentId.getPath());
            this.getBuilder(id.getPath()).parent(new AlwaysExistentModelFile(BUTTON_PARENT)).texture("texture", texture);
        } else if (item.getBlock() instanceof DecoratedTrapdoorBlock) {
            this.getBuilder(id.getPath()).parent(new AlwaysExistentModelFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_bottom")));
        } else if (item.getBlock() instanceof DecoratedDoorBlock || item.getBlock() instanceof DecoratedSign.Standing || item.getBlock() instanceof DecoratedSign.Wall) {
            this.withExistingParent(id.getPath(), GENERATED).texture("layer0", new ResourceLocation(id.getNamespace(), "item/" + id.getPath()));
        } else {
            this.getBuilder(id.getPath()).parent(new AlwaysExistentModelFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath())));
        }
    }

    private static boolean isItemStackRenderer(IItemRenderProperties properties) {
        try {
            properties.getItemStackRenderer();
        } catch (RendererOnDataGenException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
