package ltd.lemongaming.crazyenvoys.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.Warden;
import org.bukkit.entity.Witch;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * Utility class that helps us spawn geared skellies.
 */
public final class EnemyUtils {

    /**
     * Entity provider map.
     */
    private static final EnumMap<EntityType, Function<Location, Mob>> ENTITY_PROVIDERS = new EnumMap<>(EntityType.class) {{
        put(EntityType.SKELETON, location -> {
            final Skeleton skeleton = location.getWorld().spawn(location, Skeleton.class);
            return setupEntity(skeleton);
        });
        put(EntityType.BLAZE, location -> {
            final Blaze blaze = location.getWorld().spawn(location, Blaze.class);
            return setupEntity(blaze);
        });
        put(EntityType.EVOKER, location -> {
            final Evoker evoker = location.getWorld().spawn(location, Evoker.class);
            return setupEntity(evoker);
        });
        put(EntityType.VINDICATOR, location -> {
            final Vindicator vindicator = location.getWorld().spawn(location, Vindicator.class);
            return setupEntity(vindicator);
        });
        put(EntityType.WITCH, location -> {
            final Witch witch = location.getWorld().spawn(location, Witch.class);
            return setupEntity(witch);
        });
        put(EntityType.PILLAGER, location -> {
            final Pillager pillager = location.getWorld().spawn(location, Pillager.class);
            return setupEntity(pillager);
        });
        put(EntityType.RAVAGER, location -> {
            final Ravager ravager = location.getWorld().spawn(location, Ravager.class);
            ravager.addPassenger(spawnMob(EntityType.PILLAGER, location).orElseThrow());
            return setupEntity(ravager);
        });
        put(EntityType.WARDEN, location -> {
            final Warden warden = location.getWorld().spawn(location, Warden.class);
            return setupEntity(warden);
        });
    }};

    private static final EntityType[] ENTITY_TYPES = ENTITY_PROVIDERS.keySet().toArray(EntityType[]::new);

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
    private static final Material[] HELMETS = new Material[]{
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
    private static final Material[] CHESTPLATES = new Material[]{
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
    private static final Material[] BOOTS = new Material[]{
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
     * Spawn a random mob from our arsenal at the given location.
     *
     * @param location Location to spawn it at.
     * @return {@link Mob} spawned entity.
     */
    @NotNull
    public static Mob spawnRandomMob(@NotNull Location location) {
        final EntityType entityType = ENTITY_TYPES[RANDOM.nextInt(ENTITY_TYPES.length)];
        return spawnMob(entityType, location).orElseThrow();
    }

    /**
     * Spawn a mob of the given entity type with all attributes applied.
     *
     * @param entityType Type of entity to spawn.
     * @param location   Location to spawn it at.
     * @return {@link Optional} holding the {@link Mob}, empty if no instances of it exist.
     */
    @NotNull
    public static Optional<Mob> spawnMob(@NotNull EntityType entityType, @NotNull Location location) {
        if (!ENTITY_PROVIDERS.containsKey(entityType)) {
            return Optional.empty();
        }
        return Optional.of(ENTITY_PROVIDERS.get(entityType).apply(location));
    }

    /**
     * Handle the entity setup for the given mob.
     *
     * @param entity Entity to setup.
     * @return Entity with aggressive attributes setup.
     */
    private static Mob setupEntity(@NotNull Mob entity) {
        entity.setMaxHealth(DEFAULT_HEALTH);
        entity.setHealth(DEFAULT_HEALTH);

        entity.customName(Component.text("Envoy Guard!", NamedTextColor.RED, TextDecoration.BOLD));
        entity.setCustomNameVisible(true);

        entity.setCanPickupItems(false);
        entity.setAggressive(true);

        final EntityEquipment equipment = entity.getEquipment();
        equipment.setHelmet(new ItemStack(HELMETS[RANDOM.nextInt(HELMETS.length)]));
        equipment.setChestplate(new ItemStack(CHESTPLATES[RANDOM.nextInt(CHESTPLATES.length)]));
        equipment.setBoots(new ItemStack(BOOTS[RANDOM.nextInt(BOOTS.length)]));
        return entity;
    }
}