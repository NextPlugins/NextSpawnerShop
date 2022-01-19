package br.com.nextplugins.spawnershop.command.manager;

import br.com.nextplugins.spawnershop.NextSpawnerShop;
import br.com.nextplugins.spawnershop.command.SpawnerAdminCommand;
import br.com.nextplugins.spawnershop.command.SpawnerShopCommand;
import br.com.nextplugins.spawnershop.configuration.value.MessageValue;
import lombok.Data;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;

@Data(staticConstructor = "of")
public final class CommandManager {

    private final NextSpawnerShop plugin;

    public void init() {
        final BukkitFrame bukkitFrame = new BukkitFrame(plugin);

        final MessageHolder messageHolder = bukkitFrame.getMessageHolder();

        messageHolder.setMessage(MessageType.ERROR, MessageValue.get(MessageValue::executionError));
        messageHolder.setMessage(MessageType.NO_PERMISSION, MessageValue.get(MessageValue::noPermission));
        messageHolder.setMessage(MessageType.INCORRECT_TARGET, MessageValue.get(MessageValue::incorrectTarget));
        messageHolder.setMessage(MessageType.INCORRECT_USAGE, MessageValue.get(MessageValue::incorrectUsage));

        bukkitFrame.registerCommands(
            new SpawnerShopCommand(
                plugin.getSpawnerManager(),
                plugin.getDiscountManager(),
                plugin.getEconomyHook()
            ),
            new SpawnerAdminCommand(
                plugin.getConfigurationManager(),
                plugin.getUserStorage()
            )
        );
    }

}
