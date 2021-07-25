package io.github.noeppi_noeppi.libx.config;

import com.google.gson.JsonElement;
import io.github.noeppi_noeppi.libx.impl.config.ConfigImpl;
import net.minecraft.network.FriendlyByteBuf;

/**
 * A value mapper for a generic type. This will get the value mapper for the generic
 * that has been used to declare the config field. For example you could create a
 * value mapper for lists that would then get passed a value mapper for the elements
 * of the list so it can fully serialise it.
 * 
 * A limitation with these is that you can only have one generic parameter for
 * which you can get a value mapper. Only LibX builtin mappers can have multiple
 * of these.
 * 
 * When extending this, you should make the type argument {@code T} dependent on {@code C}.
 * For example for lists, you should do
 * {@code ListValueMapper&lt;T&gt; implements GenericValueMapper&lt;List&lt;T&gt;, JsonArray, T&gt;}. Then you
 * can register a {@code ListValueMapper&lt;?&gt;}.
 * 
 * @param <T> The type that this mapper can serialise.
 * @param <E> The JSON element type this mapper uses.
 * @param <C> The element type.
 */
public interface GenericValueMapper<T, E extends JsonElement, C> extends CommonValueMapper<T, E> {

    /**
     * The position which generic type argument should be used to retrieve a value mapper. Indices
     * start at 0.
     */
    int getGenericElementPosition();

    /**
     * @see ValueMapper#fromJSON(JsonElement)
     */
    T fromJSON(E json, ValueMapper<C, JsonElement> mapper);
    
    /**
     * @see ValueMapper#toJSON(Object)
     */
    E toJSON(T value, ValueMapper<C, JsonElement> mapper);

    /**
     * @see ValueMapper#read(FriendlyByteBuf)
     */
    default T read(FriendlyByteBuf buffer, ValueMapper<C, JsonElement> mapper) {
        return this.fromJSON(ConfigImpl.INTERNAL.fromJson(buffer.readUtf(0x40000), this.element()), mapper);
    }
    
    /**
     * @see ValueMapper#write(Object, FriendlyByteBuf)
     */
    default void write(T value, FriendlyByteBuf buffer, ValueMapper<C, JsonElement> mapper) {
        buffer.writeUtf(ConfigImpl.INTERNAL.toJson(this.toJSON(value, mapper)), 0x40000);
    }
}
