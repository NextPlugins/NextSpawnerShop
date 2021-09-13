package br.com.nextplugins.spawnershop.command.manager;

import br.com.nextplugins.spawnershop.NextSpawnerShop;
import br.com.nextplugins.spawnershop.command.SpawnerAdminCommand;
import br.com.nextplugins.spawnershop.command.SpawnerShopCommand;
import lombok.Data;
import me.saiintbrisson.bukkit.command.BukkitFrame;

@Data(staticConstructor = "of")
public final class CommandManager {

    private final NextSpawnerShop plugin;

    public void init() {
        final BukkitFrame bukkitFrame = new BukkitFrame(plugin);

        bukkitFrame.registerCommands(
            new SpawnerShopCommand(
                plugin.getSpawnerManager(),
                plugin.getDiscountManager(),
                plugin.getEconomyHook()
            ),
            new SpawnerAdminCommand(
                plugin.getConfigurationManager(),
                plugin.getConfigurationManager().getMessagesConfiguration().getConfigurationSection("messages")
            )
        );
    }

}
