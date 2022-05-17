package andrews.table_top_craft.util.loot_table;

import andrews.table_top_craft.util.Reference;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class TTCLootTableHandler
{
    private static final Set<ResourceLocation> JUNGLE_TEMPLE_INJECTIONS = Sets.newHashSet(BuiltInLootTables.JUNGLE_TEMPLE);
    private static final Set<ResourceLocation> DESERT_PYRAMID_INJECTIONS = Sets.newHashSet(BuiltInLootTables.DESERT_PYRAMID);
    private static final Set<ResourceLocation> ABANDONED_MINESHAFT_INJECTIONS = Sets.newHashSet(BuiltInLootTables.ABANDONED_MINESHAFT);
    private static final Set<ResourceLocation> SIMPLE_DUNGEON_INJECTIONS = Sets.newHashSet(BuiltInLootTables.SIMPLE_DUNGEON);
    private static final Set<ResourceLocation> NETHER_BRIDGE_INJECTIONS = Sets.newHashSet(BuiltInLootTables.NETHER_BRIDGE);

    @SubscribeEvent
    public static void onInjectLoot(LootTableLoadEvent event)
    {
        if(JUNGLE_TEMPLE_INJECTIONS.contains(event.getName()))
        {
            LootPool pool = LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(Reference.MODID, "injections/chess_piece_jungle_temple"))).build();
            event.getTable().addPool(pool);
        }
        if(DESERT_PYRAMID_INJECTIONS.contains(event.getName()))
        {
            LootPool pool = LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(Reference.MODID, "injections/chess_piece_desert_pyramid"))).build();
            event.getTable().addPool(pool);
        }
        if(ABANDONED_MINESHAFT_INJECTIONS.contains(event.getName()))
        {
            LootPool pool = LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(Reference.MODID, "injections/chess_piece_abandoned_mineshaft"))).build();
            event.getTable().addPool(pool);
        }
        if(SIMPLE_DUNGEON_INJECTIONS.contains(event.getName()))
        {
            LootPool pool = LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(Reference.MODID, "injections/chess_piece_simple_dungeon"))).build();
            event.getTable().addPool(pool);
        }
        if(NETHER_BRIDGE_INJECTIONS.contains(event.getName()))
        {
            LootPool pool = LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(Reference.MODID, "injections/chess_piece_nether_bridge"))).build();
            event.getTable().addPool(pool);
        }
    }
}