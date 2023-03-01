package org.moddingx.vanilla;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class TemplatePools {

    private TemplatePools() {}

    private static final ResourceKey<Registry<StructureTemplatePool>> REGISTRY = ResourceKey.createRegistryKey(new ResourceLocation("minecraft","worldgen/template_pool"));

    public static final ResourceKey<StructureTemplatePool> ANCIENT_CITY_CITY_ENTRANCE = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","ancient_city/city/entrance"));
    public static final ResourceKey<StructureTemplatePool> ANCIENT_CITY_CITY_CENTER = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","ancient_city/city_center"));
    public static final ResourceKey<StructureTemplatePool> ANCIENT_CITY_CITY_CENTER_WALLS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","ancient_city/city_center/walls"));
    public static final ResourceKey<StructureTemplatePool> ANCIENT_CITY_SCULK = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","ancient_city/sculk"));
    public static final ResourceKey<StructureTemplatePool> ANCIENT_CITY_STRUCTURES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","ancient_city/structures"));
    public static final ResourceKey<StructureTemplatePool> ANCIENT_CITY_WALLS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","ancient_city/walls"));
    public static final ResourceKey<StructureTemplatePool> ANCIENT_CITY_WALLS_NO_CORNERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","ancient_city/walls/no_corners"));
    public static final ResourceKey<StructureTemplatePool> BASTION_BLOCKS_GOLD = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/blocks/gold"));
    public static final ResourceKey<StructureTemplatePool> BASTION_BRIDGE_BRIDGE_PIECES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/bridge/bridge_pieces"));
    public static final ResourceKey<StructureTemplatePool> BASTION_BRIDGE_CONNECTORS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/bridge/connectors"));
    public static final ResourceKey<StructureTemplatePool> BASTION_BRIDGE_LEGS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/bridge/legs"));
    public static final ResourceKey<StructureTemplatePool> BASTION_BRIDGE_RAMPART_PLATES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/bridge/rampart_plates"));
    public static final ResourceKey<StructureTemplatePool> BASTION_BRIDGE_RAMPARTS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/bridge/ramparts"));
    public static final ResourceKey<StructureTemplatePool> BASTION_BRIDGE_STARTING_PIECES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/bridge/starting_pieces"));
    public static final ResourceKey<StructureTemplatePool> BASTION_BRIDGE_WALLS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/bridge/walls"));
    public static final ResourceKey<StructureTemplatePool> BASTION_HOGLIN_STABLE_CONNECTORS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/hoglin_stable/connectors"));
    public static final ResourceKey<StructureTemplatePool> BASTION_HOGLIN_STABLE_LARGE_STABLES_INNER = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/hoglin_stable/large_stables/inner"));
    public static final ResourceKey<StructureTemplatePool> BASTION_HOGLIN_STABLE_LARGE_STABLES_OUTER = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/hoglin_stable/large_stables/outer"));
    public static final ResourceKey<StructureTemplatePool> BASTION_HOGLIN_STABLE_MIRRORED_STARTING_PIECES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/hoglin_stable/mirrored_starting_pieces"));
    public static final ResourceKey<StructureTemplatePool> BASTION_HOGLIN_STABLE_POSTS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/hoglin_stable/posts"));
    public static final ResourceKey<StructureTemplatePool> BASTION_HOGLIN_STABLE_RAMPART_PLATES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/hoglin_stable/rampart_plates"));
    public static final ResourceKey<StructureTemplatePool> BASTION_HOGLIN_STABLE_RAMPARTS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/hoglin_stable/ramparts"));
    public static final ResourceKey<StructureTemplatePool> BASTION_HOGLIN_STABLE_SMALL_STABLES_INNER = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/hoglin_stable/small_stables/inner"));
    public static final ResourceKey<StructureTemplatePool> BASTION_HOGLIN_STABLE_SMALL_STABLES_OUTER = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/hoglin_stable/small_stables/outer"));
    public static final ResourceKey<StructureTemplatePool> BASTION_HOGLIN_STABLE_STAIRS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/hoglin_stable/stairs"));
    public static final ResourceKey<StructureTemplatePool> BASTION_HOGLIN_STABLE_STARTING_PIECES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/hoglin_stable/starting_pieces"));
    public static final ResourceKey<StructureTemplatePool> BASTION_HOGLIN_STABLE_WALL_BASES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/hoglin_stable/wall_bases"));
    public static final ResourceKey<StructureTemplatePool> BASTION_HOGLIN_STABLE_WALLS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/hoglin_stable/walls"));
    public static final ResourceKey<StructureTemplatePool> BASTION_MOBS_HOGLIN = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/mobs/hoglin"));
    public static final ResourceKey<StructureTemplatePool> BASTION_MOBS_PIGLIN = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/mobs/piglin"));
    public static final ResourceKey<StructureTemplatePool> BASTION_MOBS_PIGLIN_MELEE = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/mobs/piglin_melee"));
    public static final ResourceKey<StructureTemplatePool> BASTION_STARTS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/starts"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_BASES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/bases"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_BASES_CENTERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/bases/centers"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_BRAINS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/brains"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_CONNECTORS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/connectors"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_CORNERS_BOTTOM = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/corners/bottom"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_CORNERS_EDGES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/corners/edges"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_CORNERS_MIDDLE = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/corners/middle"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_CORNERS_TOP = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/corners/top"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_ENTRANCES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/entrances"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_EXTENSIONS_HOUSES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/extensions/houses"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_EXTENSIONS_LARGE_POOL = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/extensions/large_pool"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_EXTENSIONS_SMALL_POOL = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/extensions/small_pool"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_RAMPARTS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/ramparts"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_ROOFS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/roofs"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_STAIRS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/stairs"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_WALLS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/walls"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_WALLS_BOTTOM = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/walls/bottom"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_WALLS_MID = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/walls/mid"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_WALLS_OUTER = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/walls/outer"));
    public static final ResourceKey<StructureTemplatePool> BASTION_TREASURE_WALLS_TOP = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/treasure/walls/top"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_CENTER_PIECES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/center_pieces"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_EDGE_WALL_UNITS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/edge_wall_units"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_EDGES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/edges"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_FILLERS_STAGE_0 = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/fillers/stage_0"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_LARGE_RAMPARTS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/large_ramparts"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_PATHWAYS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/pathways"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_RAMPART_PLATES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/rampart_plates"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_RAMPARTS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/ramparts"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_STAGES_ROT_STAGE_1 = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/stages/rot/stage_1"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_STAGES_STAGE_0 = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/stages/stage_0"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_STAGES_STAGE_1 = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/stages/stage_1"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_STAGES_STAGE_2 = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/stages/stage_2"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_STAGES_STAGE_3 = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/stages/stage_3"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_WALL_UNITS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/wall_units"));
    public static final ResourceKey<StructureTemplatePool> BASTION_UNITS_WALLS_WALL_BASES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","bastion/units/walls/wall_bases"));
    public static final ResourceKey<StructureTemplatePool> EMPTY = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","empty"));
    public static final ResourceKey<StructureTemplatePool> PILLAGER_OUTPOST_BASE_PLATES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","pillager_outpost/base_plates"));
    public static final ResourceKey<StructureTemplatePool> PILLAGER_OUTPOST_FEATURE_PLATES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","pillager_outpost/feature_plates"));
    public static final ResourceKey<StructureTemplatePool> PILLAGER_OUTPOST_FEATURES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","pillager_outpost/features"));
    public static final ResourceKey<StructureTemplatePool> PILLAGER_OUTPOST_TOWERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","pillager_outpost/towers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_COMMON_ANIMALS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/common/animals"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_COMMON_BUTCHER_ANIMALS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/common/butcher_animals"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_COMMON_CATS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/common/cats"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_COMMON_IRON_GOLEM = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/common/iron_golem"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_COMMON_SHEEP = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/common/sheep"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_COMMON_WELL_BOTTOMS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/common/well_bottoms"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_DESERT_CAMEL = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/desert/camel"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_DESERT_DECOR = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/desert/decor"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_DESERT_HOUSES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/desert/houses"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_DESERT_STREETS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/desert/streets"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_DESERT_TERMINATORS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/desert/terminators"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_DESERT_TOWN_CENTERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/desert/town_centers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_DESERT_VILLAGERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/desert/villagers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_DESERT_ZOMBIE_DECOR = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/desert/zombie/decor"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_DESERT_ZOMBIE_HOUSES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/desert/zombie/houses"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_DESERT_ZOMBIE_STREETS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/desert/zombie/streets"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_DESERT_ZOMBIE_TERMINATORS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/desert/zombie/terminators"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_DESERT_ZOMBIE_VILLAGERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/desert/zombie/villagers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_PLAINS_DECOR = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/plains/decor"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_PLAINS_HOUSES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/plains/houses"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_PLAINS_STREETS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/plains/streets"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_PLAINS_TERMINATORS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/plains/terminators"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_PLAINS_TOWN_CENTERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/plains/town_centers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_PLAINS_TREES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/plains/trees"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_PLAINS_VILLAGERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/plains/villagers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_PLAINS_ZOMBIE_DECOR = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/plains/zombie/decor"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_PLAINS_ZOMBIE_HOUSES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/plains/zombie/houses"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_PLAINS_ZOMBIE_STREETS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/plains/zombie/streets"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_PLAINS_ZOMBIE_VILLAGERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/plains/zombie/villagers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SAVANNA_DECOR = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/savanna/decor"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SAVANNA_HOUSES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/savanna/houses"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SAVANNA_STREETS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/savanna/streets"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SAVANNA_TERMINATORS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/savanna/terminators"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SAVANNA_TOWN_CENTERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/savanna/town_centers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SAVANNA_TREES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/savanna/trees"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SAVANNA_VILLAGERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/savanna/villagers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SAVANNA_ZOMBIE_DECOR = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/savanna/zombie/decor"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SAVANNA_ZOMBIE_HOUSES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/savanna/zombie/houses"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SAVANNA_ZOMBIE_STREETS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/savanna/zombie/streets"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SAVANNA_ZOMBIE_TERMINATORS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/savanna/zombie/terminators"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SAVANNA_ZOMBIE_VILLAGERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/savanna/zombie/villagers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SNOWY_DECOR = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/snowy/decor"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SNOWY_HOUSES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/snowy/houses"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SNOWY_STREETS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/snowy/streets"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SNOWY_TERMINATORS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/snowy/terminators"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SNOWY_TOWN_CENTERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/snowy/town_centers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SNOWY_TREES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/snowy/trees"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SNOWY_VILLAGERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/snowy/villagers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SNOWY_ZOMBIE_DECOR = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/snowy/zombie/decor"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SNOWY_ZOMBIE_HOUSES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/snowy/zombie/houses"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SNOWY_ZOMBIE_STREETS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/snowy/zombie/streets"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_SNOWY_ZOMBIE_VILLAGERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/snowy/zombie/villagers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_TAIGA_DECOR = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/taiga/decor"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_TAIGA_HOUSES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/taiga/houses"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_TAIGA_STREETS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/taiga/streets"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_TAIGA_TERMINATORS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/taiga/terminators"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_TAIGA_TOWN_CENTERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/taiga/town_centers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_TAIGA_VILLAGERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/taiga/villagers"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_TAIGA_ZOMBIE_DECOR = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/taiga/zombie/decor"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_TAIGA_ZOMBIE_HOUSES = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/taiga/zombie/houses"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_TAIGA_ZOMBIE_STREETS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/taiga/zombie/streets"));
    public static final ResourceKey<StructureTemplatePool> VILLAGE_TAIGA_ZOMBIE_VILLAGERS = ResourceKey.create(REGISTRY, new ResourceLocation("minecraft","village/taiga/zombie/villagers"));
}
