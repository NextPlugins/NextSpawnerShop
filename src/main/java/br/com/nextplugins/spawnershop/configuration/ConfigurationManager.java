package br.com.nextplugins.spawnershop.configuration;

import br.com.nextplugins.spawnershop.NextSpawnerShop;
import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Getter
@RequiredArgsConstructor
public final class ConfigurationManager {

    private final NextSpawnerShop plugin;

    private FileConfiguration spawnersConfiguration;
    private FileConfiguration discountsConfiguration;
    private FileConfiguration messagesConfiguration;

    public void init() {
        files();
    }

    private void files() {
        plugin.saveResource("spawners.yml", false);
        plugin.saveResource("discounts.yml", false);
        plugin.saveResource("messages.yml", false);

        this.spawnersConfiguration = loadConfiguration("spawners.yml");
        this.discountsConfiguration = loadConfiguration("discounts.yml");
        this.messagesConfiguration = loadConfiguration("messages.yml");
    }

    @SneakyThrows
    public String tryReloadAllAndReturnCallbackMessage() {
        final CompletableFuture<String> reloadFuture = CompletableFuture.supplyAsync(() -> {
            try {
                final Stopwatch timer = Stopwatch.createStarted();

                plugin.reloadConfig();

                this.spawnersConfiguration = loadConfiguration("spawners.yml");
                plugin.getSpawnerManager().loadSpawners(true);

                this.discountsConfiguration = loadConfiguration("discounts.yml");
                plugin.getDiscountManager().loadDiscounts(true);

                this.messagesConfiguration = loadConfiguration("messages.yml");

                timer.stop();

                return ChatColor.GREEN + String.format("Foram recarregados todos os arquivos de configurações do plugin. (%sms)",
                    timer.elapsed(TimeUnit.MILLISECONDS)
                );
            } catch (Throwable t) {
                t.printStackTrace();
                return ChatColor.RED + "Ocorreu um erro durante o recarregamento dos arquivos de configurações.";
            }
        });

        return reloadFuture.get();
    }

    @SneakyThrows
    private FileConfiguration loadConfiguration(String name) {
        final File file = new File(plugin.getDataFolder(), name);

        return YamlConfiguration.loadConfiguration(file);
    }

}
