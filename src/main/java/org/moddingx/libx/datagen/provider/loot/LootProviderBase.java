package org.moddingx.libx.datagen.provider.loot;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.AlternativeLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.IForgeRegistry;
import org.moddingx.libx.datagen.LootBuilders;
import org.moddingx.libx.datagen.provider.loot.entry.GenericLootModifier;
import org.moddingx.libx.datagen.provider.loot.entry.LootFactory;
import org.moddingx.libx.datagen.provider.loot.entry.LootModifier;
import org.moddingx.libx.datagen.provider.loot.entry.SimpleLootFactory;
import org.moddingx.libx.impl.data.LootData;
import org.moddingx.libx.mod.ModX;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class LootProviderBase<T> implements DataProvider {

    protected final ModX mod;
    protected final DataGenerator generator;
    protected final String folder;
    protected final LootContextParamSet params;
    protected final Supplier<Stream<Map.Entry<ResourceLocation, T>>> elements;

    private final Set<T> ignored = new HashSet<>();
    private final Map<T, Function<T, LootTable.Builder>> functionMap = new HashMap<>();

    protected LootProviderBase(ModX mod, DataGenerator generator, String folder, LootContextParamSet params, IForgeRegistry<T> registry) {
        this(mod, generator, folder, params, () -> registry.getEntries().stream().map(entry -> Map.entry(entry.getKey().location(), entry.getValue())));
    }
    
    protected LootProviderBase(ModX mod, DataGenerator generator, String folder, LootContextParamSet params, Supplier<Stream<Map.Entry<ResourceLocation, T>>> elements) {
        this.mod = mod;
        this.generator = generator;
        this.folder = folder;
        this.params = params;
        this.elements = elements;
    }

    protected abstract void setup();

    /**
     * The given item will not be processed by this provider. Useful when you want to create the loot table manually.
     */
    protected void customLootTable(T item) {
        this.ignored.add(item);
    }

    /**
     * The given item will get the given loot table.
     */
    protected void customLootTable(T item, LootTable.Builder loot) {
        this.functionMap.put(item, b -> loot);
    }

    /**
     * The given item will get the given loot table function.
     */
    protected void customLootTable(T item, Function<T, LootTable.Builder> loot) {
        this.functionMap.put(item, loot);
    }

    /**
     * Gets the element factory. The element factory is a loot factory that assigns
     * each element a loot entry. The default just assigns an empty entry.
     */
    protected SimpleLootFactory<T> element() {
        return SimpleLootFactory.from(EmptyLootItem.emptyItem());
    }
    
    /**
     * Creates a default loot table for the given item. Can be overridden to alter
     * default behaviour. Should return null if no loot table should be generated.
     */
    @Nullable
    protected abstract LootTable.Builder defaultBehavior(T item);

    @Nonnull
    @Override
    public String getName() {
        ResourceLocation key = LootContextParamSets.getKey(this.params);
        String name = key == null ? "generic" : ("minecraft".equals(key.getNamespace()) ? key.getPath() : key.toString());
        return this.mod.modid + " " + name + " loot tables";
    }
    
    @Override
    public void run(@Nonnull CachedOutput cache) throws IOException {
        this.setup();
        
        Map<ResourceLocation, LootTable.Builder> tables = this.elements.get()
                .filter(entry -> this.mod.modid.equals(entry.getKey().getNamespace()))
                .filter(entry -> !this.ignored.contains(entry.getValue()))
                .flatMap(this::resolve)
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

        for (Map.Entry<ResourceLocation, LootTable.Builder> e : tables.entrySet()) {
            Path path = this.getPath(this.generator.getOutputFolder(), e.getKey());
            DataProvider.saveStable(cache, LootTables.serialize(e.getValue().setParamSet(this.params).build()), path);
        }
    }
    
    private Stream<Map.Entry<ResourceLocation, LootTable.Builder>> resolve(Map.Entry<ResourceLocation, T> entry) {
        Function<T, LootTable.Builder> loot;
        if (this.functionMap.containsKey(entry.getValue())) {
            loot = this.functionMap.get(entry.getValue());
        } else {
            LootTable.Builder builder = this.defaultBehavior(entry.getValue());
            loot = builder == null ? null : b -> builder;
        }
        return loot == null ? Stream.empty() : Stream.of(Map.entry(entry.getKey(), loot.apply(entry.getValue())));
    }
    
    private Path getPath(Path root, ResourceLocation id) {
        return root.resolve("data/" + id.getNamespace() + "/loot_tables/" + this.folder + "/" + id.getPath() + ".json");
    }
    
    protected final LootModifier<T> modifier(BiFunction<T, LootPoolSingletonContainer.Builder<?>, LootPoolSingletonContainer.Builder<?>> function) {
        return LootModifier.of(this.element(), function);
    }
    
    protected final GenericLootModifier<T> genericModifier(BiFunction<T, LootPoolSingletonContainer.Builder<?>, LootPoolEntryContainer.Builder<?>> function) {
        return GenericLootModifier.of(this.element(), function);
    }
    
    protected final LootModifier<T> identity() {
        return LootModifier.identity(this.element());
    }

    /**
     * Method to add a custom loot table for an item.
     */
    public void drops(T item, ItemStack... drops) {
        this.drops(item, Arrays.stream(drops).<LootFactory<T>>map(this::stack).toList());
    }
    
    /**
     * Method to add a custom loot table for an item.
     */
    @SafeVarargs
    public final void drops(T item, LootFactory<T>... loot) {
        this.drops(item, Arrays.stream(loot).toList());
    }
    
    /**
     * Method to add a custom loot table for an item.
     */
    public void drops(T item, List<LootFactory<T>> loot) {
        this.generateBaseTable(item, this.combine(loot).build(item));
    }
    
    /**
     * Generate the base loot table.
     */
    public void generateBaseTable(T item, LootPoolEntryContainer.Builder<?> entry) {
        LootPool.Builder pool = LootPool.lootPool().name("main")
                .setRolls(ConstantValue.exactly(1)).add(entry);
        this.customLootTable(item, LootTable.lootTable().withPool(pool));
    }
    
    /**
     * Turns a singleton loot entry into a simple loot factory.
     */
    public SimpleLootFactory<T> from(LootPoolSingletonContainer.Builder<?> entry) {
        return SimpleLootFactory.from(entry);
    }

    /**
     * Turns a loot entry into a loot factory.
     */
    public LootFactory<T> from(LootPoolEntryContainer.Builder<?> entry) {
        return LootFactory.from(entry);
    }

    /**
     * Turns a loot function into a loot modifier.
     */
    public LootModifier<T> from(LootItemConditionalFunction.Builder<?> function) {
        return this.modifier((item, entry) -> entry.apply(function));
    }

    /**
     * A condition that is random with a chance.
     */
    public LootItemCondition.Builder random(float chance) {
        return LootItemRandomChanceCondition.randomChance(chance);
    }

    /**
     * A loot modifier that sets the count of a stack.
     */
    public LootModifier<T> count(int count) {
        return this.from(SetItemCountFunction.setCount(ConstantValue.exactly(count)));
    }

    /**
     * A loot modifier that uniformly sets the count of a stack between two values.
     */
    public LootModifier<T> count(int min, int max) {
        if (min == max) {
            return this.from(SetItemCountFunction.setCount(ConstantValue.exactly(min)));
        } else {
            return this.from(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
        }
    }

    /**
     * A loot modifier that sets the count of a stack based on a binomial formula.
     */
    public LootModifier<T> countBinomial(float chance, int num) {
        return this.from(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(num, chance)));
    }

    /**
     * Inverts a loot condition
     */
    public LootItemCondition.Builder not(LootItemCondition.Builder condition) {
        return InvertedLootItemCondition.invert(condition);
    }

    /**
     * Joins conditions with OR.
     */
    public LootItemCondition.Builder or(LootItemCondition.Builder... conditions) {
        return AlternativeLootItemCondition.alternative(conditions);
    }

    /**
     * A loot factory for a specific item.
     */
    public SimpleLootFactory<T> stack(ItemLike item) {
        return this.from(LootItem.lootTableItem(item));
    }

    /**
     * Tries to create the best possible representation of stack in a loot entry.
     */
    public SimpleLootFactory<T> stack(ItemStack stack) {
        return this.from(LootData.stack(stack));
    }
    
    /**
     * Combines the given loot factories into one. (All loot factories will be applied).
     */
    @SafeVarargs
    public final LootFactory<T> combine(LootFactory<T>... loot) {
        return this.combine(Arrays.stream(loot).toList());
    }
    
    /**
     * Combines the given loot factories into one. (All loot factories will be applied).
     */
    public final LootFactory<T> combine(List<LootFactory<T>> loot) {
        return e -> LootData.combineBy(LootBuilders::all, l -> l.build(e), loot);
    }

    /**
     * Combines the given loot factories into one. (One loot factory will be applied).
     */
    @SafeVarargs
    public final LootFactory<T> random(LootFactory<T>... loot) {
        return this.random(Arrays.stream(loot).toList());
    
    }
    
    /**
     * Combines the given loot factories into one. (One loot factory will be applied).
     */
    public final LootFactory<T> random(List<LootFactory<T>> loot) {
        return e -> LootData.combineBy(LootBuilders::group, l -> l.build(e), loot);
    }

    /**
     * Combines the given loot factories into one. Only the first matching factory is applied.
     */
    @SafeVarargs
    public final LootFactory<T> first(LootFactory<T>... loot) {
        return this.first(Arrays.stream(loot).toList());
    
    }
    
    /**
     * Combines the given loot factories into one. Only the first matching factory is applied.
     */
    public final LootFactory<T> first(List<LootFactory<T>> loot) {
        return e -> LootData.combineBy(LootBuilders::alternative, l -> l.build(e), loot);
    }
    
    /**
     * Combines the given loot factories into one.
     * From all the loot factories until the first one not matching, one is selected.
     */
    @SafeVarargs
    public final LootFactory<T> whileMatch(LootFactory<T>... loot) {
        return this.whileMatch(Arrays.stream(loot).toList());
    }
    
    /**
     * Combines the given loot factories into one.
     * From all the loot factories until the first one not matching, one is selected.
     */
    public final LootFactory<T> whileMatch(List<LootFactory<T>> loot) {
        return e -> LootData.combineBy(LootBuilders::sequence, l -> l.build(e), loot);
    }
}