package ltd.lemongaming.crazyenvoys.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class that helps us spawn geared skellies.
 */
public final class EnemyUtils {

    /**
     * Our random provider.
     */
    private static final Random RANDOM = ThreadLocalRandom.current();

    /**
     * The amount of health we will assign to them.
     */
    private static final double DEFAULT_HEALTH = 200;

    /**
     * Helmets they can spawn with.
     */
    private static final Material[] HELMETS = new Material[] {
        Material.AIR,
        Material.LEATHER_HELMET,
        Material.CHAINMAIL_HELMET,
        Material.GOLDEN_HELMET,
        Material.IRON_HELMET,
        Material.DIAMOND_HELMET
    };

    /**
     * Chestplates they can spawn with.
     */
    private static final Material[] CHESTPLATES = new Material[] {
        Material.AIR,
        Material.LEATHER_CHESTPLATE,
        Material.CHAINMAIL_CHESTPLATE,
        Material.GOLDEN_CHESTPLATE,
        Material.IRON_CHESTPLATE,
        Material.DIAMOND_CHESTPLATE,
    };

    /**
     * Boots they can spawn with.
     */
    private static final Material[] BOOTS = new Material[] {
        Material.AIR,
        Material.LEATHER_BOOTS,
        Material.CHAINMAIL_BOOTS,
        Material.GOLDEN_BOOTS,
        Material.IRON_BOOTS,
        Material.DIAMOND_BOOTS,
    };

    private EnemyUtils() {
        // prevent initialization!
    }

    /**
     * Spawn a randomly geared skelly in the place you wish!
     *
     * @param location Location to spawn the skelly in.
     * @return {@link Skeleton} spawned.
     */
    public static Skeleton spawnRandomSkeleton(@NotNull Location location) {
        final Skeleton skeleton = location.getWorld().spawn(location, Skeleton.class);

        skeleton.setMaxHealth(DEFAULT_HEALTH);
        skeleton.setHealth(DEFAULT_HEALTH);

        skeleton.setCustomName(ChatColor.values()[RANDOM.nextInt(ChatColor.values().length)] + "Envoy Guard!");
        skeleton.setCustomNameVisible(true);

        skeleton.setCanPickupItems(false);
        skeleton.setAggressive(true);

        final EntityEquipment equipment = skeleton.getEquipment();
        equipment.setHelmet(new ItemStack(HELMETS[RANDOM.nextInt(HELMETS.length)]));
        equipment.setChestplate(new ItemStack(CHESTPLATES[RANDOM.nextInt(CHESTPLATES.length)]));
        equipment.setBoots(new ItemStack(BOOTS[RANDOM.nextInt(BOOTS.length)]));

        return skeleton;
    }
}