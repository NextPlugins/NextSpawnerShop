package br.com.nextplugins.spawnershop.manager;

import br.com.nextplugins.spawnershop.NextSpawnerShop;
import br.com.nextplugins.spawnershop.api.model.spawner.Spawner;
import br.com.nextplugins.spawnershop.api.model.spawner.data.SpawnerData;
import br.com.nextplugins.spawnershop.util.item.ItemParser;
import br.com.nextplugins.spawnershop.util.time.DateParser;
import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public final class SpawnerManager {

    private final NextSpawnerShop plugin;

    @Getter private final Map<String, Spawner> spawners = new LinkedHashMap<>();

    @Getter private final Map<UUID, Spawner> playersBuyingSpawners = new LinkedHashMap<>();

    public void init() {
        loadSpawners(false);
    }

    public void loadSpawners(boolean fromReload) {
        final Stopwatch timer = Stopwatch.createStarted();

        if (fromReload) spawners.clear();

        final FileConfiguration configuration = plugin.getConfigurationManager().getSpawnersConfiguration();
        final ConfigurationSection spawnersSection = configuration.getConfigurationSection("spawners");

        if (spawnersSection == null) return;

        final Set<String> keys = spawnersSection.getKeys(false);

        for (String key : keys) {
            spawners.put(
                key,
                Spawner.builder()
                    .id(key)
                    .data(
                        SpawnerData.builder()
                            .cost(spawnersSection.getDouble(key + ".cost.coins"))
                            .cashCost(spawnersSection.getDouble(key + ".cost.cash"))
                            .icon(new ItemParser().parseSection(spawnersSection.getConfigurationSection(key + ".icon")))
                            .commands(spawnersSection.getStringList(key + ".commands"))
                            .build()
                    )
                    .releaseDate(
                        Objects.requireNonNull(spawnersSection.getString(key + ".release-at")).isEmpty()
                            ? null
                            : DateParser.parse(spawnersSection.getString(key + ".release-at"))
                    )
                    .build()
            );
        }

        timer.stop();

        if (!fromReload) {
            final Logger logger = plugin.getLogger();

            logger.log(Level.INFO, "Todos os spawners foram carregados e cacheados. ({0}ms)", timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }

}
