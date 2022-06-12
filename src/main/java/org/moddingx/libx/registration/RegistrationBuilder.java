package org.moddingx.libx.registration;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import org.moddingx.libx.impl.registration.resolution.ForgeRegistryResolver;
import org.moddingx.libx.impl.registration.resolution.VanillaRegistryResolver;
import org.moddingx.libx.registration.resolution.RegistryResolver;
import org.moddingx.libx.registration.resolution.ResolvedRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A registration builder is used to configure the LibX registration system and adjust the behaviour of it.
 */
public class RegistrationBuilder {
    
    private boolean tracking;
    private final List<RegistryResolver> resolvers;
    private final List<RegistryCondition> conditions;
    private final List<RegistryTransformer> transformers;
    
    public RegistrationBuilder() {
        this.tracking = false;
        this.resolvers = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.transformers = new ArrayList<>();
    }

    /**
     * Enables automatic registry tracking. That means when registering objects, 
     * {@link Registerable#initTracking(RegistrationContext, Registerable.TrackingCollector)}
     * is called. In order for registry tracking to properly work, you need to manually track the fields
     * holding your values. ModInit will do this automatically if this option here is enabled.
     */
    public void enableRegistryTracking() {
        this.tracking = true;
    }
    
    /**
     * Adds a {@link RegistryResolver}. The resolvers are queried in the order they were added. The first
     * resolver that matches a registry will be used. By default there are resolvers for forge registries,
     * {@link Registry vanilla registries} and {@link BuiltinRegistries vanilla builtin registries}.
     */
    public void resolve(RegistryResolver resolver) {
        this.resolvers.add(resolver);
    }

    /**
     * Adds a registry resolver that resolves to the given registry for the given registry key.
     */
    public <T> void resolve(ResourceKey<? extends Registry<T>> id, ResolvedRegistry<T> registry) {
        this.resolve(new RegistryResolver() {
            
            @Override
            public <A> Optional<ResolvedRegistry<A>> resolve(ResourceKey<? extends Registry<A>> key) {
                //noinspection unchecked
                return id.equals(key) ? Optional.of((ResolvedRegistry<A>) registry) : Optional.empty();
            }
        });
    }
    
    /**
     * Adds a registry resolver that resolves to the given registry for the given registry key.
     */
    public <T> void resolve(ResourceKey<? extends Registry<T>> id, Registry<T> registry) {
        this.resolve(id, new ResolvedRegistry.Vanilla<>(registry));
    }

    /**
     * Adds a new {@link RegistryCondition} that must match each object that is passed to the system in order
     * to be registered.
     */
    public void condition(RegistryCondition condition) {
        this.conditions.add(condition);
    }
    
    /**
     * Adds a new {@link RegistryTransformer} that can add additional objects that are registered with each
     * object registered through the LibX registration system.
     */
    public void transformer(RegistryTransformer transformer) {
        this.transformers.add(transformer);
    }
    
    public Result build() {
        List<RegistryResolver> resolvers = Stream.concat(this.resolvers.stream(), Stream.of(
                ForgeRegistryResolver.INSTANCE,
                new VanillaRegistryResolver(Registry.REGISTRY),
                new VanillaRegistryResolver(BuiltinRegistries.REGISTRY)
        )).toList();
        return new Result(this.tracking, resolvers, List.copyOf(this.conditions), List.copyOf(this.transformers));
    }
    
    public static record Result(boolean tracking, List<RegistryResolver> resolvers, List<RegistryCondition> conditions, List<RegistryTransformer> transformers) {}
}
