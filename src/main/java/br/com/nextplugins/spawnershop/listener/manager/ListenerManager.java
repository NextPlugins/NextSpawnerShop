package br.com.nextplugins.spawnershop.listener.manager;

import br.com.nextplugins.spawnershop.NextSpawnerShop;
import br.com.nextplugins.spawnershop.listener.SpawnerBuyListener;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

@Data(staticConstructor = "of")
public final class ListenerManager {

    private final NextSpawnerShop plugin;

    public void init() {
        final PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new SpawnerBuyListener(
            plugin.getEconomyHook()
        ), plugin);
    }

}
