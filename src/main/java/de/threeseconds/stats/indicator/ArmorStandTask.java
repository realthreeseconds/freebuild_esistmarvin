package de.threeseconds.stats.indicator;

import de.threeseconds.FreeBuild;
import org.jetbrains.annotations.NotNull;

public record ArmorStandTask(@NotNull IndicatorManager indicatorManager) implements Runnable {

    @Override
    public void run() {
        var iterator = this.indicatorManager.activeArmorStands().entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            var armorStand = entry.getKey();
            var spawnTime = entry.getValue();

            if (System.currentTimeMillis() - spawnTime > 1500) {
                armorStand.remove();
                iterator.remove();
                continue;
            }

            armorStand.teleport(armorStand.getLocation().subtract(0, 0.04, 0));


        }
    }
}
