package org.moddingx.libx.impl.datagen.patchouli.content;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.moddingx.libx.datagen.provider.patchouli.content.CaptionContent;
import org.moddingx.libx.datagen.provider.patchouli.page.PageBuilder;

import javax.annotation.Nullable;
import java.util.Objects;

public class EntityContent extends CaptionContent {

    private final EntityType<?> entity;
    private final Float scale;
    private final Float offset;
    private final Boolean rotate;

    public EntityContent(EntityType<?> entity) {
        this(entity, null, null, null, null);
    }

    public EntityContent(EntityType<?> entity, @Nullable Float scale, @Nullable Float offset, @Nullable Boolean rotate){
        this(entity, null, scale, offset, rotate);
    }

    private EntityContent(EntityType<?> entity, @Nullable String caption, @Nullable Float scale, @Nullable Float offset, @Nullable Boolean rotate) {
        super(caption);
        this.entity = entity;
        this.scale = scale;
        this.offset = offset;
        this.rotate = rotate;
    }

    @Override
    protected int lineSkip() {
        return 13;
    }

    @Override
    protected CaptionContent withCaption(String caption) {
        return new EntityContent(this.entity, caption, this.scale, this.offset, this.rotate);
    }

    @Override
    protected void specialPage(PageBuilder builder, @Nullable String caption) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "patchouli:entity");
        json.addProperty("entity", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(this.entity), "Entity not registered: " + this.entity).toString());
        if (caption != null) {
            json.addProperty("text", builder.translate(caption));
        }
        if (scale != null) {
            json.addProperty("scale", this.scale);
        }
        if (offset != null) {
            json.addProperty("offset", this.offset);
        }
        if (rotate != null) {
            json.addProperty("rotate", this.rotate);
        }
        builder.addPage(json);
    }
}
