package net.azisaba.breakdrop.listener;

import net.azisaba.breakdrop.BreakDrop;
import net.azisaba.breakdrop.config.ConfigDrop;
import net.azisaba.breakdrop.config.ConfigDropFunction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class BreakListener implements Listener {
    private final BreakDrop plugin;

    public BreakListener(@NotNull BreakDrop plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(@NotNull BlockBreakEvent e) {
        for (ConfigDrop drop : plugin.getPluginConfig().getDrops()) {
            if (!drop.canExecute(e.getPlayer(), e.getBlock())) {
                continue;
            }
            for (ConfigDropFunction function : drop.getFunctions()) {
                if (!function.canExecute(e.getPlayer(), e.getBlock())) {
                    continue;
                }
                for (int i = 0; i < function.getCount(); i++) {
                    if (!function.rollChance()) {
                        continue;
                    }
                    function.execute(e.getPlayer(), e.getBlock());
                }
            }
        }
    }
}
