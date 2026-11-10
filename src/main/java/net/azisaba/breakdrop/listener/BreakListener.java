package net.azisaba.breakdrop.listener;

import com.gmail.nossr50.mcMMO;
import net.azisaba.breakdrop.BreakDrop;
import net.azisaba.breakdrop.config.ConfigDrop;
import net.azisaba.breakdrop.config.ConfigDropFunction;
import net.azisaba.breakdrop.hook.WorldGuardHook;
import net.azisaba.breakdrop.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class BreakListener implements Listener {
    private static final Set<String> EXCLUDED_LIFE_ITEM_ID = Collections.singleton("56fabea9-e1f9-4e7f-ae78-83e07e8b8767");
    private final BreakDrop plugin;

    public BreakListener(@NotNull BreakDrop plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(@NotNull BlockBreakEvent e) {
        var mainLifeItemId = ItemUtil.getStringTag(e.getPlayer().getInventory().getItemInMainHand(), "LifeItemId");
        var offLifeItemId = ItemUtil.getStringTag(e.getPlayer().getInventory().getItemInOffHand(), "LifeItemId");
        if ((mainLifeItemId != null && EXCLUDED_LIFE_ITEM_ID.contains(mainLifeItemId)) || (offLifeItemId != null && EXCLUDED_LIFE_ITEM_ID.contains(offLifeItemId))) return;

        if (Bukkit.getPluginManager().isPluginEnabled("mcMMO") && checkPlaceStore(e.getBlock().getState())) {
            return;
        }

        // 他プラグイン(範囲破壊など)が保護チェックなしでBlockBreakEventを発火する場合があるため、WorldGuardの保護を直接確認する
        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard") && !WorldGuardHook.canBreak(e.getPlayer(), e.getBlock())) {
            return;
        }

        for (ConfigDrop drop : plugin.getPluginConfig().getDrops()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (!drop.canExecute(e.getPlayer(), e.getBlock())) {
                    return;
                }
                for (ConfigDropFunction function : drop.getFunctions()) {
                    if (!function.canExecute(e.getPlayer(), e.getBlock())) {
                        continue;
                    }
                    for (int i = 0; i < function.getCount(); i++) {
                        if (!function.rollChance()) {
                            continue;
                        }
                        Bukkit.getScheduler().runTask(plugin, () -> function.execute(e.getPlayer(), e.getBlock()));
                    }
                }
            });
        }
    }

    @SuppressWarnings("removal")
    private static boolean checkPlaceStore(BlockState blockState) {
        return mcMMO.getPlaceStore().isTrue(blockState);
    }
}
