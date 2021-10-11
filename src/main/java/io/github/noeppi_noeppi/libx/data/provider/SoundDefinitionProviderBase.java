package io.github.noeppi_noeppi.libx.data.provider;

import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * A base class for sound definition providers.
 */
public abstract class SoundDefinitionProviderBase implements DataProvider {

    protected final ModX mod;
    
    // Keep the real provider as a field instead of extending it
    // because of conflicting methods.
    private final ParentProvider provider;

    private final Set<ResourceLocation> blacklist = new HashSet<>();
    private final Map<ResourceLocation, SoundDefinitionBuilder> sounds = new HashMap<>();

    public SoundDefinitionProviderBase(ModX mod, DataGenerator generator, ExistingFileHelper helper) {
        this.mod = mod;
        this.provider = new ParentProvider(generator, mod.modid, helper) {
            
            @Override
            public void registerSounds() {
                SoundDefinitionProviderBase.this.registerSounds();
            }
        };
    }

    @Nonnull
    @Override
    public String getName() {
        return this.mod.modid + " sound definitions";
    }

    /**
     * This sound will not be processed by the default generator
     */
    protected void ignore(SoundEvent sound) {
        this.ignore(Objects.requireNonNull(sound.getRegistryName()));
    }
    
    /**
     * This sound will not be processed by the default generator
     */
    protected void ignore(ResourceLocation sound) {
        this.blacklist.add(sound);
    }

    protected abstract void setup();

    protected void defaultSound(ResourceLocation id, SoundEvent sound) {
        this.sound(sound)
                .subtitle("subtitle." + id.getNamespace() + "." + id.getPath().replace("/", "."))
                .with(id);
    }
    
    protected SoundSettingsBuilder settings() {
        return new SoundSettingsBuilder();
    }
    
    protected SoundDefinitionBuilder sound(SoundEvent sound) {
        return this.sound(Objects.requireNonNull(sound.getRegistryName()), this.settings());
    }
    
    protected SoundDefinitionBuilder sound(ResourceLocation sound) {
        return this.sound(sound, this.settings());
    }
    
    protected SoundDefinitionBuilder sound(SoundEvent sound, SoundSettingsBuilder settings) {
        return this.sound(Objects.requireNonNull(sound.getRegistryName()), settings);
    }
    
    protected SoundDefinitionBuilder sound(ResourceLocation sound, SoundSettingsBuilder settings) {
        this.ignore(sound);
        if (this.sounds.containsKey(sound)) throw new IllegalArgumentException("Sound processed twice: " + sound);
        SoundDefinitionBuilder builder = new SoundDefinitionBuilder(settings);
        this.sounds.put(sound, builder);
        return builder;
    }
    
    private void registerSounds() {
        this.setup();

        for (ResourceLocation id : ForgeRegistries.SOUND_EVENTS.getKeys()) {
            if (this.mod.modid.equals(id.getNamespace()) && !this.blacklist.contains(id)) {
                SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(id);
                if (sound != null) {
                    this.defaultSound(id, sound);
                }
            }
        }
        
        for (Map.Entry<ResourceLocation, SoundDefinitionBuilder> entry : this.sounds.entrySet()) {
            this.provider.add(entry.getKey(), entry.getValue().definition);
        }
    }

    @Override
    public void run(@Nonnull HashCache cache) throws IOException {
        this.provider.run(cache);
    }

    protected static class SoundSettingsBuilder {

        private float volume = 1;
        private float pitch = 1;
        private int weight = 1;
        private boolean stream = false;
        private int attenuationDistance = 16;
        private boolean preload = false;

        private SoundSettingsBuilder() {

        }
        
        public SoundSettingsBuilder volume(float volume) {
            this.volume = volume;
            return this;
        }


        public SoundSettingsBuilder pitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public SoundSettingsBuilder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public SoundSettingsBuilder stream() {
            return this.stream(true);
        }

        public SoundSettingsBuilder stream(boolean stream) {
            this.stream = stream;
            return this;
        }

        public SoundSettingsBuilder attenuationDistance(int attenuationDistance) {
            this.attenuationDistance = attenuationDistance;
            return this;
        }

        public SoundSettingsBuilder preload() {
            return this.preload(true);
        }

        public SoundSettingsBuilder preload(boolean preload) {
            this.preload = preload;
            return this;
        }
        
        private void applyTo(SoundDefinition.Sound sound) {
            sound.volume(this.volume);
            sound.pitch(this.pitch);
            sound.weight(this.weight);
            sound.stream(this.stream);
            sound.attenuationDistance(this.attenuationDistance);
            sound.preload(this.preload);
        }
    }
    
    protected class SoundDefinitionBuilder {

        private final SoundSettingsBuilder settings;
        private final SoundDefinition definition;
        
        private SoundDefinitionBuilder(SoundSettingsBuilder settings) {
            this.settings = settings;
            this.definition = SoundDefinition.definition();
        }

        public SoundDefinitionBuilder replace() {
            return this.replace(true);
        }
        
        public SoundDefinitionBuilder replace(boolean replace) {
            this.definition.replace(replace);
            return this;
        }
        
        public SoundDefinitionBuilder subtitle(@Nullable String subtitle) {
            this.definition.subtitle(subtitle);
            return this;
        }

        public SoundDefinitionBuilder with(String path) {
            return this.with(SoundDefinitionProviderBase.this.mod.resource(path), sound -> {});
        }

        public SoundDefinitionBuilder with(ResourceLocation soundId) {
            return this.with(soundId, sound -> {});
        }
        
        public SoundDefinitionBuilder with(String path, Consumer<SoundDefinition.Sound> configure) {
            return this.with(SoundDefinitionProviderBase.this.mod.resource(path), configure);
        }
        
        public SoundDefinitionBuilder with(ResourceLocation soundId, Consumer<SoundDefinition.Sound> configure) {
            SoundDefinition.Sound sound = SoundDefinition.Sound.sound(soundId, SoundDefinition.SoundType.SOUND);
            this.settings.applyTo(sound);
            configure.accept(sound);
            this.definition.with(sound);
            return this;
        }
        
        public SoundDefinitionBuilder withRange(String path, int amount) {
            return this.withRange(SoundDefinitionProviderBase.this.mod.resource(path), amount, sound -> {});
        }

        public SoundDefinitionBuilder withRange(ResourceLocation soundId, int amount) {
            return this.withRange(soundId, amount, sound -> {});
        }
        
        public SoundDefinitionBuilder withRange(String path, int amount, Consumer<SoundDefinition.Sound> configure) {
            return this.withRange(SoundDefinitionProviderBase.this.mod.resource(path), amount, configure);
        }
        
        public SoundDefinitionBuilder withRange(ResourceLocation soundId, int amount, Consumer<SoundDefinition.Sound> configure) {
            for (int i = 0; i < amount; i++) {
                this.with(new ResourceLocation(soundId.getNamespace(), soundId.getPath() + i), configure);
            }
            return this;
        }
        
        public SoundDefinitionBuilder effect(SoundEvent event) {
            return this.effect(event, sound -> {});
        }
        
        public SoundDefinitionBuilder effect(SoundEvent event, Consumer<SoundDefinition.Sound> configure) {
            SoundDefinition.Sound sound = SoundDefinition.Sound.sound(Objects.requireNonNull(event.getRegistryName()), SoundDefinition.SoundType.SOUND);
            this.settings.applyTo(sound);
            configure.accept(sound);
            this.definition.with(sound);
            return this;
        }
    }
    
    // Required to make a method public
    private static abstract class ParentProvider extends SoundDefinitionsProvider {
        
        protected ParentProvider(DataGenerator generator, String modId, ExistingFileHelper helper) {
            super(generator, modId, helper);
        }

        @Override
        public void add(@Nonnull ResourceLocation sound, @Nonnull SoundDefinition definition) {
            super.add(sound, definition);
        }
    }
}
