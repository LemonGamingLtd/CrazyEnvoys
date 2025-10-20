package com.badbones69.crazyenvoys.listeners;

import com.badbones69.crazyenvoys.CrazyEnvoys;
import com.badbones69.crazyenvoys.api.CrazyManager;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.NotNull;

public class EntityListener implements Listener {

    private final @NotNull CrazyEnvoys plugin = CrazyEnvoys.get();
    private final @NotNull CrazyManager crazyManager = this.plugin.getCrazyManager();

    @EventHandler
    public void onEntityTargetEvent(@NotNull EntityTargetEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Entity entity = event.getEntity();
        final Entity target = event.getTarget();
        if (this.crazyManager.getNpcs().contains(entity) && this.crazyManager.getNpcs().contains(target)) {
            event.setCancelled(true);
            event.setTarget(null);
        }
    }

    @EventHandler
    public void onEntityDeathEvent(@NotNull EntityDeathEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Entity entity = event.getEntity();
        if (this.crazyManager.getNpcs().contains(entity)) {
            event.setDroppedExp(0);
            event.getDrops().clear();

            this.crazyManager.getNpcs().remove(entity);
        }
    }
}
