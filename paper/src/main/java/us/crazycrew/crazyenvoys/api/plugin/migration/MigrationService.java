package us.crazycrew.crazyenvoys.api.plugin.migration;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.badbones69.crazyenvoys.CrazyEnvoys;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazyenvoys.common.config.types.ConfigKeys;
import us.crazycrew.crazyenvoys.common.config.types.MessageKeys;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MigrationService {

    private final @NotNull CrazyEnvoys plugin = CrazyEnvoys.get();

    public void migrate() {
        // Migrate some options from the plugin-config.yml back to config.yml
        copyPluginConfig();

        // Migrate what's left from Config-Backup.yml to config.yml then delete the backup file.
        copyConfig();

        // Migrate all messages to en-US.yml then delete Messages.yml
        copyMessages();

        // Rename file if found.
        File file = new File(this.plugin.getDataFolder(), "data.yml");
        if (file.exists()) file.renameTo(new File(this.plugin.getDataFolder(), "users.yml"));
    }

    private void copyPluginConfig() {
        File input = new File(this.plugin.getDataFolder(), "plugin-config.yml");

        if (!input.exists()) return;

        YamlConfiguration config = CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(input)).join();

        YamlFileResourceOptions builder = YamlFileResourceOptions.builder().indentationSize(2).build();

        SettingsManager configKeys = SettingsManagerBuilder
                .withYamlFile(new File(this.plugin.getDataFolder(), "config.yml"), builder)
                .useDefaultMigrationService()
                .configurationData(ConfigKeys.class)
                .create();

        String language = config.getString("language", "en-US");

        boolean verbose = config.getBoolean("verbose_logging", false);
        boolean metrics = config.getBoolean("toggle_metrics", false);

        String prefix = config.getString("command_prefix", "&8[&dCrazyEnvoys&8]: ");
        String consolePrefix = config.getString("console_prefix", "&8[&cCrazyEnvoys&8] ");

        configKeys.setProperty(ConfigKeys.locale_file, language);
        configKeys.setProperty(ConfigKeys.verbose_logging, verbose);
        configKeys.setProperty(ConfigKeys.toggle_metrics, metrics);
        configKeys.setProperty(ConfigKeys.command_prefix, prefix);
        configKeys.setProperty(ConfigKeys.console_prefix, consolePrefix);

        // Save to file.
        configKeys.save();

        // Delete old file.
        if (input.delete()) this.plugin.getLogger().warning("Successfully migrated " + input.getName() + ".yml");
    }

    private void copyConfig() {
        File input = new File(this.plugin.getDataFolder(), "config.yml");

        YamlConfiguration old = CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(input)).join();
        
        // If the config configuration doesn't have this. we do nothing.
        if (old.getString("Settings.Falling-Block-Toggle") == null) return;
        
        // Rename config.yml to this.
        File backupFile = new File(this.plugin.getDataFolder(), "Config-Backup.yml");
        input.renameTo(backupFile);

        YamlConfiguration file = CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(backupFile)).join();

        String oldPrefix = file.getString("Settings.Prefix", "&8[&cCrazyEnvoys&8]: ");
        boolean oldMetrics = file.getBoolean("Settings.Toggle-Metrics", false);

        boolean fallingBlockToggle = file.getBoolean("Settings.Falling-Block-Toggle");

        String fallingBlockType = file.getString("Settings.Falling-Block");

        int fallingBlockHeight = file.getInt("Settings.Fall-Height");

        boolean maxCrateToggle = file.getBoolean("Settings.Max-Crate-Toggle");

        boolean randomAmount = file.getBoolean("Settings.Random-Amount");

        int minCrates = file.getInt("Settings.Min-Crates");

        int maxCrates = file.getInt("Settings.Max-Crates");

        boolean randomLocations = file.getBoolean("Settings.Random-Locations");

        int maxRadius = file.getInt("Settings.Max-Radius");

        int minRadius = file.getInt("Settings.Min-Radius");

        boolean envoyLocationsBroadcast = file.getBoolean("Settings.Envoy-Locations.Broadcast");

        String envoyRunTime = file.getString("Settings.Envoy-Run-Time");

        boolean envoyTimerToggle = file.getBoolean("Settings.Envoy-Timer-Toggle");

        boolean envoyCooldownToggle = file.getBoolean("Settings.Envoy-Cooldown-Toggle");

        boolean envoyFilterPLayers = file.getBoolean("Settings.Envoy-Filter-Players-Zero");

        String envoyCooldown = file.getString("Settings.Envoy-Cooldown");
        String envoyTime = file.getString("Settings.Envoy-Time");

        boolean minimumPlayersToggle = file.getBoolean("Settings.Minimum-Players-Toggle");

        boolean minimumFlareToggle = file.getBoolean("Settings.Minimum-Flare-Toggle");

        int minimumPlayersRequired = file.getInt("Settings.Minimum-Players");

        boolean broadcastEnvoyPickUp = file.getBoolean("Settings.Broadcast-Crate-Pick-Up");

        boolean envoyCollectCooldownToggle = file.getBoolean("Settings.Crate-Collect-Cooldown.Toggle");

        String envoyCollectCooldownTime = file.getString("Settings.Crate-Collect-Cooldown.Time");

        boolean envoyCountdownToggle = file.getBoolean("Settings.Crate-Countdown.Toggle");
        int envoyCountdownTime = file.getInt("Settings.Crate-Countdown.Time");

        String envoyCountdownMessage = file.getString("Settings.Crate-Countdown.Message");

        String envoyCountdownMessageOther = file.getString("Settings.Crate-Countdown.Message-Seconds");

        boolean envoyWorldMessages = file.getBoolean("Settings.World-Messages.Toggle");

        List<String> envoyAllowedWorlds = file.getStringList("Settings.World-Messages.Worlds");

        List<String> envoyWarnings = file.getStringList("Settings.Envoy-Warnings");

        String envoyFlareItem = file.getString("Settings.Flares.Item");

        String envoyFlareName = file.getString("Settings.Flares.Name");

        List<String> envoyFlareItemLore = file.getStringList("Settings.Flares.Lore");

        boolean envoyFlareWorldGuard = file.getBoolean("Settings.Flares.World-Guard.Toggle");

        List<String> envoyFlareWorldGuardRegions = file.getStringList("Settings.Flares.World-Guard.Regions");

        YamlFileResourceOptions builder = YamlFileResourceOptions.builder().indentationSize(2).build();

        SettingsManager configKeys = SettingsManagerBuilder
                .withYamlFile(new File(this.plugin.getDataFolder(), "config.yml"), builder)
                .useDefaultMigrationService()
                .configurationData(ConfigKeys.class)
                .create();

        configKeys.setProperty(ConfigKeys.command_prefix, oldPrefix);
        configKeys.setProperty(ConfigKeys.toggle_metrics, oldMetrics);

        configKeys.setProperty(ConfigKeys.envoy_falling_block_toggle, fallingBlockToggle);
        configKeys.setProperty(ConfigKeys.envoy_falling_block_type, fallingBlockType);
        configKeys.setProperty(ConfigKeys.envoy_falling_height, fallingBlockHeight);

        configKeys.setProperty(ConfigKeys.envoys_max_drops_toggle, maxCrateToggle);
        configKeys.setProperty(ConfigKeys.envoys_max_drops, maxCrates);
        configKeys.setProperty(ConfigKeys.envoys_min_drops, minCrates);

        configKeys.setProperty(ConfigKeys.envoys_random_drops, randomAmount);

        configKeys.setProperty(ConfigKeys.envoys_random_locations, randomLocations);
        configKeys.setProperty(ConfigKeys.envoys_max_radius, maxRadius);
        configKeys.setProperty(ConfigKeys.envoys_min_radius, minRadius);

        configKeys.setProperty(ConfigKeys.envoys_locations_broadcast, envoyLocationsBroadcast);

        configKeys.setProperty(ConfigKeys.envoys_run_time_toggle, envoyTimerToggle);
        configKeys.setProperty(ConfigKeys.envoys_run_time, envoyRunTime);

        configKeys.setProperty(ConfigKeys.envoys_countdown, envoyCooldownToggle);
        configKeys.setProperty(ConfigKeys.envoys_cooldown, envoyCooldown);
        configKeys.setProperty(ConfigKeys.envoys_time, envoyTime);

        configKeys.setProperty(ConfigKeys.envoys_ignore_empty_server, envoyFilterPLayers);

        configKeys.setProperty(ConfigKeys.envoys_minimum_players_toggle, minimumPlayersToggle);
        configKeys.setProperty(ConfigKeys.envoys_minimum_players_amount, minimumPlayersRequired);

        configKeys.setProperty(ConfigKeys.envoys_flare_minimum_players_toggle, minimumFlareToggle);
        configKeys.setProperty(ConfigKeys.envoys_flare_minimum_players_amount, minimumPlayersRequired);

        configKeys.setProperty(ConfigKeys.envoys_flare_item_name, envoyFlareName);
        configKeys.setProperty(ConfigKeys.envoys_flare_item_type, envoyFlareItem);
        configKeys.setProperty(ConfigKeys.envoys_flare_item_lore, envoyFlareItemLore);
        configKeys.setProperty(ConfigKeys.envoys_flare_world_guard_toggle, envoyFlareWorldGuard);
        configKeys.setProperty(ConfigKeys.envoys_flare_world_guard_regions, envoyFlareWorldGuardRegions);

        configKeys.setProperty(ConfigKeys.envoys_announce_player_pickup, broadcastEnvoyPickUp);

        configKeys.setProperty(ConfigKeys.envoys_grab_cooldown_toggle, envoyCollectCooldownToggle);
        configKeys.setProperty(ConfigKeys.envoys_grab_cooldown_timer, envoyCollectCooldownTime);

        configKeys.setProperty(ConfigKeys.envoys_grace_period_toggle, envoyCountdownToggle);
        configKeys.setProperty(ConfigKeys.envoys_grace_period_timer, envoyCountdownTime);
        configKeys.setProperty(ConfigKeys.envoys_grace_period_unlocked, envoyCountdownMessage);
        configKeys.setProperty(ConfigKeys.envoys_grace_period_time_unit, envoyCountdownMessageOther);

        configKeys.setProperty(ConfigKeys.envoys_world_messages, envoyWorldMessages);
        configKeys.setProperty(ConfigKeys.envoys_allowed_worlds, envoyAllowedWorlds);
        configKeys.setProperty(ConfigKeys.envoys_warnings, envoyWarnings);

        // Save new config.
        configKeys.save();

        // Delete old file.
        backupFile.delete();
    }

    private void copyMessages() {
        File input = new File(this.plugin.getDataFolder(), "Messages.yml");

        // If the input does not exist, We don't need to do anything else.
        if (!input.exists()) return;

        // Load configuration of input.
        YamlConfiguration file = CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(input)).join();

        // Check if directory exists and create it if not.
        File localeDir = new File(this.plugin.getDataFolder(), "locale");
        if (!localeDir.exists()) localeDir.mkdirs();

        YamlFileResourceOptions builder = YamlFileResourceOptions.builder().indentationSize(2).build();

        SettingsManager configKeys = SettingsManagerBuilder
                .withYamlFile(new File(this.plugin.getDataFolder(), "config.yml"), builder)
                .useDefaultMigrationService()
                .configurationData(ConfigKeys.class)
                .create();

        // Create messages file.
        File messagesFile = new File(localeDir, configKeys.getProperty(ConfigKeys.locale_file) + ".yml");
        SettingsManager messages = SettingsManagerBuilder
                .withYamlFile(messagesFile)
                .useDefaultMigrationService()
                .configurationData(MessageKeys.class)
                .create();
        
        String noPermission = convert("{prefix}" + file.getString("Messages.No-Permission"));
        String noClaimPermission = convert("{prefix}" + file.getString("Messages.No-Permission-Claim"));
        
        String playerOnly = convert("{prefix}" + file.getString("Messages.Players-Only"));
        String notOnline = convert("{prefix}" + file.getString("Messages.Not-Online"));
        String notANumber = convert("{prefix}" + file.getString("Messages.Not-A-Number"));
        
        String reloaded = convert("{prefix}" + file.getString("Messages.Reloaded"));
        
        String alreadyStarted = convert("{prefix}" + file.getString("Messages.Already-Started"));
        String forceStart = convert("{prefix}" + file.getString("Messages.Force-Start"));
        String notStarted = convert("{prefix}" + file.getString("Messages.Not-Started"));
        String forceEnded = convert("{prefix}" + file.getString("Messages.Force-Ended"));
        
        String warning = convert("{prefix}" + file.getString("Messages.Warning"));
        List<String> started = file.getStringList("Messages.Started");
        String left = convert("{prefix}" + file.getString("Messages.Left"));
        String ended = convert("{prefix}" + file.getString("Messages.Ended"));
        
        String notEnoughPlayers = convert("{prefix}" + file.getString("Messages.Not-Enough-Players"));
        
        String enterEditorMode = convert("{prefix}" + file.getString("Messages.Enter-Editor-Mode"));
        String leaveEditorMode = convert("{prefix}" + file.getString("Messages.Leave-Editor-Mode"));
        
        String editorClearLocations = convert("{prefix}" + file.getString("Messages.Editor-Clear-Locations"));
        String editorClearFailure = convert("{prefix}" + file.getString("Messages.Editor-Clear-Failure"));
        
        String kickedFromEditor = convert("{prefix}" + file.getString("Messages.Kicked-From-Editor-Mode"));
        
        String addLocation = convert("{prefix}" + file.getString("Messages.Add-Location"));
        String removeLocation = convert("{prefix}" + file.getString("Messages.Remove-Location"));
        
        String timeLeft = convert("{prefix}" + file.getString("Messages.Time-Left"));
        String timeTillEvent = convert("{prefix}" + file.getString("Messages.Time-Till-Event"));
        String usedFlare = convert("{prefix}" + file.getString("Messages.Used-Flare"));
        
        String cantUseFlares = convert("{prefix}" + file.getString("Messages.Cant-Use-Flares"));
        String giveFlare = convert("{prefix}" + file.getString("Messages.Give-Flare"));
        String givenFlare = convert("{prefix}" + file.getString("Messages.Given-Flare"));
        
        String newCenter = convert("{prefix}" + file.getString("Messages.New-Center"));
        
        String notInWorldGuardRegion = convert("{prefix}" + file.getString("Messages.Not-In-World-Guard-Region"));
        
        String startIgnoringMessages = convert("{prefix}" + file.getString("Messages.Start-Ignoring-Messages"));
        String stopIgnoringMessages = convert("{prefix}" + file.getString("Messages.Stop-Ignoring-Messages"));
        
        String cooldownLeft = convert("{prefix}" + file.getString("Messages.Cooldown-Left"));
        
        String countdownInProgress = convert("{prefix}" + file.getString("Messages.Countdown-In-Progress"));
        
        String dropsAvailable = convert("{prefix}" + file.getString("Messages.Drops-Available"));
        String dropsPossible = convert("{prefix}" + file.getString("Messages.Drops-Possibilities"));
        
        String dropsPage = convert("{prefix}" + file.getString("Messages.Drops-Page"));
        
        String dropsFormat = convert("{prefix}" + file.getString("Messages.Drops-Format"));
        
        String noSpawnLocations = convert("{prefix}" + file.getString("Messages.No-Spawn-Locations-Found"));
        
        String commandNotFound = convert("{prefix}" + file.getString("Messages.Command-Not-Found"));
        
        String hologramOnGoing = convert("{prefix}" + file.getString("Messages.Hologram-Placeholders.On-Going"));
        String hologramNotRunning = convert("{prefix}" + file.getString("Messages.Hologram-Placeholders.Not-Running"));
        
        String timeDay = convert(file.getString("Messages.Time-Placeholders.Day"));
        String timeHour = convert(file.getString("Messages.Time-Placeholders.Hour"));
        String timeMinute = convert(file.getString("Messages.Time-Placeholders.Minute"));
        String timeSecond = convert(file.getString("Messages.Time-Placeholders.Second"));
        
        String crateLocations = convert("{prefix}" + file.getString("Messages.Crate-Locations"));
        
        String locationFormat = convert("{prefix}" + file.getString("Messages.Location-Format"));
        
        List<String> help = file.getStringList("Messages.Help");

        messages.setProperty(MessageKeys.no_permission, noPermission);
        messages.setProperty(MessageKeys.no_claim_permission, noClaimPermission);

        messages.setProperty(MessageKeys.envoy_already_started, alreadyStarted);
        messages.setProperty(MessageKeys.envoy_force_start, forceStart);
        messages.setProperty(MessageKeys.envoy_not_started, notStarted);
        messages.setProperty(MessageKeys.envoy_force_ended, forceEnded);

        messages.setProperty(MessageKeys.envoy_warning, warning);
        messages.setProperty(MessageKeys.envoy_started, started);
        messages.setProperty(MessageKeys.envoys_remaining, left);
        messages.setProperty(MessageKeys.envoy_ended, ended);

        messages.setProperty(MessageKeys.not_enough_players, notEnoughPlayers);

        messages.setProperty(MessageKeys.enter_editor_mode, enterEditorMode);
        messages.setProperty(MessageKeys.exit_editor_mode, leaveEditorMode);
        messages.setProperty(MessageKeys.envoy_clear_locations, editorClearLocations);
        messages.setProperty(MessageKeys.envoy_clear_failure, editorClearFailure);

        messages.setProperty(MessageKeys.envoy_kicked_from_editor_mode, kickedFromEditor);

        messages.setProperty(MessageKeys.envoy_add_location, addLocation);
        messages.setProperty(MessageKeys.envoy_remove_location, removeLocation);

        messages.setProperty(MessageKeys.envoy_time_left, timeLeft);
        messages.setProperty(MessageKeys.envoy_time_till_event, timeTillEvent);

        messages.setProperty(MessageKeys.envoy_used_flare, usedFlare);
        messages.setProperty(MessageKeys.envoy_cant_use_flare, cantUseFlares);
        messages.setProperty(MessageKeys.envoy_give_flare, giveFlare);
        messages.setProperty(MessageKeys.envoy_received_flare, givenFlare);

        messages.setProperty(MessageKeys.envoy_new_center, newCenter);

        messages.setProperty(MessageKeys.not_in_world_guard_region, notInWorldGuardRegion);

        messages.setProperty(MessageKeys.start_ignoring_messages, startIgnoringMessages);
        messages.setProperty(MessageKeys.stop_ignoring_messages, stopIgnoringMessages);

        messages.setProperty(MessageKeys.cooldown_left, cooldownLeft);

        messages.setProperty(MessageKeys.countdown_in_progress, countdownInProgress);

        messages.setProperty(MessageKeys.drops_available, dropsAvailable);
        messages.setProperty(MessageKeys.drops_possibilities, dropsPossible);
        messages.setProperty(MessageKeys.drops_page, dropsPage);
        messages.setProperty(MessageKeys.drops_format, dropsFormat);

        messages.setProperty(MessageKeys.no_spawn_locations_found, noSpawnLocations);

        messages.setProperty(MessageKeys.hologram_on_going, hologramOnGoing);
        messages.setProperty(MessageKeys.hologram_not_running, hologramNotRunning);

        messages.setProperty(MessageKeys.time_placeholder_day, timeDay);
        messages.setProperty(MessageKeys.time_placeholder_hour, timeHour);
        messages.setProperty(MessageKeys.time_placeholder_minute, timeMinute);
        messages.setProperty(MessageKeys.time_placeholder_second, timeSecond);

        messages.setProperty(MessageKeys.envoy_locations, crateLocations);
        messages.setProperty(MessageKeys.location_format, locationFormat);
        messages.setProperty(MessageKeys.command_not_found, commandNotFound);
        messages.setProperty(MessageKeys.player_only, playerOnly);
        messages.setProperty(MessageKeys.not_online, notOnline);
        messages.setProperty(MessageKeys.not_a_number, notANumber);
        messages.setProperty(MessageKeys.envoy_plugin_reloaded, reloaded);

        messages.setProperty(MessageKeys.help, help);

        // Save the file.
        messages.save();

        // Delete the Messages.yml
        input.delete();
    }

    private String convert(String message) {
        return message
                .replaceAll("%prefix%", "")
                .replaceAll("%Prefix%", "")
                .replaceAll("%time%", "{time}")
                .replaceAll("%amount%", "{amount}")
                .replaceAll("%tier%", "{tier}")
                .replaceAll("%player%", "{player}")
                .replaceAll("%world%", "{world}")
                .replaceAll("%x%", "{x}")
                .replaceAll("%y%", "{y}")
                .replaceAll("%z%", "{z}")
                .replaceAll("%id%", "{id}")
                .replaceAll("%locations%", "{locations}");
    }
}