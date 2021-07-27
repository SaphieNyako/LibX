package io.github.noeppi_noeppi.libx.impl.config.wrapper;

import com.google.gson.JsonElement;
import io.github.noeppi_noeppi.libx.config.ValueMapper;
import net.minecraft.network.FriendlyByteBuf;

public class TypesafeMapper extends JsonTypesafeMapper<Object> {
    
    public TypesafeMapper(ValueMapper<?, ?> mapper) {
        //noinspection unchecked
        super((ValueMapper<Object, ?>) mapper);
    }

    @Override
    public JsonElement toJSON(Object value) {
        if (this.wrapped.type().isAssignableFrom(value.getClass())) {
            return this.wrapped.toJSON(value);
        } else {
            throw new IllegalArgumentException("Type mismatch in config mapper toJSON: Expected " + this.wrapped.type() + ", got " + value.getClass());
        }
    }

    @Override
    public void write(Object value, FriendlyByteBuf buffer) {
        if (this.wrapped.type().isAssignableFrom(value.getClass())) {
            this.wrapped.write(value, buffer);
        } else {
            throw new IllegalArgumentException("Type mismatch in config mapper write: Expected " + this.wrapped.type() + ", got " + value.getClass());
        }
    }
}