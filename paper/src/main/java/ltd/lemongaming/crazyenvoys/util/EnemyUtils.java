package ltd.lemongaming.crazyenvoys.util;

import com.badbones69.crazyenvoys.support.SkullCreator;
import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * Utility class that helps us spawn geared skellies.
 */
public final class EnemyUtils {

    /**
     * Entity provider map.
     */
    private static final EnumMap<EntityType, Function<Location, Mob>> ENTITY_PROVIDERS = new EnumMap<>(EntityType.class);

    static {
        addEntityType(EntityType.SKELETON);
    }

    private static final EntityType[] ENTITY_TYPES = ENTITY_PROVIDERS.keySet().toArray(EntityType[]::new);

    /**
     * Our random provider.
     */
    private static final Random RANDOM = ThreadLocalRandom.current();

    /**
     * The amount of health we will assign to them.
     */
    private static final double DEFAULT_HEALTH = 20;

    /**
     * Helmets they can spawn with.
     */
    private static final ItemStack[] HELMETS = new ItemStack[]{
        // bandit
        new SkullCreator().itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE2YjM5NmYxMzJhMjgwYmQwNjE5OTYwNTg2NGYyNmIwMDk4MzNlOGI2MmY4OTM0M2Y4NDIyMWJmNDg5YjZhNCJ9fX0="),
        // new ItemStack(Material.LEATHER_HELMET),
        // new ItemStack(Material.CHAINMAIL_HELMET),
        // new ItemStack(Material.GOLDEN_HELMET),
        // new ItemStack(Material.IRON_HELMET),
        // new ItemStack(Material.DIAMOND_HELMET),
    };

    /**
     * Chestplates they can spawn with.
     */
    private static final ItemStack[] CHESTPLATES = new ItemStack[]{
        new ItemStack(Material.AIR),
        new ItemStack(Material.CHAINMAIL_CHESTPLATE),
    };

    /**
     * Leggings they can spawn with.
     */
    private static final ItemStack[] LEGGINGS = new ItemStack[]{
        new ItemStack(Material.AIR),
        new ItemStack(Material.CHAINMAIL_LEGGINGS),
    };

    /**
     * Boots they can spawn with.
     */
    private static final ItemStack[] BOOTS = new ItemStack[]{
        new ItemStack(Material.AIR),
        new ItemStack(Material.CHAINMAIL_BOOTS),
    };

    private static final ItemStack[] MAIN_HAND = new ItemStack[]{
        new ItemStack(Material.STONE_SWORD),
        new ItemStack(Material.STONE_AXE),
        new ItemStack(Material.BOW),
    };

    /**
     * Potion effects.
     */
    private static final Collection<PotionEffect> POTION_EFFECTS = Set.of(
        PotionEffectType.SPEED.createEffect(PotionEffect.INFINITE_DURATION, 1),
        PotionEffectType.JUMP.createEffect(PotionEffect.INFINITE_DURATION, 1)
    );

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
     * Streamline the entity type addition process.
     *
     * @param entityType {@link EntityType} entity type to handle.
     */
    private static void addEntityType(@NotNull EntityType entityType) {
        Preconditions.checkArgument(entityType.isAlive(), "entity type cannot be a non-alive entity!");
        ENTITY_PROVIDERS.put(entityType, location -> {
            final Mob mob = (Mob) location.getWorld().spawn(location, entityType.getEntityClass()); // surely
            return setupEntity(mob);
        });
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

        entity.customName(Component.text("Bandit"));
        entity.setCustomNameVisible(true);

        entity.setCanPickupItems(false);
        entity.setAggressive(true);

        entity.clearLootTable();

        final EntityEquipment equipment = entity.getEquipment();
        equipment.setHelmet(HELMETS[RANDOM.nextInt(HELMETS.length)].clone());
        equipment.setChestplate(CHESTPLATES[RANDOM.nextInt(CHESTPLATES.length)].clone());
        equipment.setLeggings(LEGGINGS[RANDOM.nextInt(LEGGINGS.length)].clone());
        equipment.setBoots(BOOTS[RANDOM.nextInt(BOOTS.length)].clone());
        equipment.setItemInMainHand(MAIN_HAND[RANDOM.nextInt(MAIN_HAND.length)].clone());

        entity.addPotionEffects(POTION_EFFECTS);
        return entity;
    }
}
