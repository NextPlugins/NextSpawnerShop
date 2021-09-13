package br.com.nextplugins.spawnershop.command;

import br.com.nextplugins.spawnershop.configuration.ConfigurationManager;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

@RequiredArgsConstructor
public final class SpawnerAdminCommand {

    private final ConfigurationManager configurationManager;
    private final ConfigurationSection messages;

    @Command(
        name = "spawneradmin",
        description = "Comandos administrativos do sistema.",
        permission = "nextspawnershop.command.spawneradmin",
        async = true
    )
    public void handleAdmin(Context<CommandSender> context) {
        final CommandSender sender = context.getSender();

        for (String message : messages.getStringList("staff-help")) {
            sender.sendMessage(colored(message));
        }
    }

    @Command(
        name = "spawneradmin.reload",
        description = "Recarregue os arquivos de configurações.",
        permission = "nextspawnershop.command.spawneradmin.reload",
        async = true
    )
    public void handleAdminReload(Context<CommandSender> context) {
        final CommandSender sender = context.getSender();

        final String callbackMessage = configurationManager.tryReloadAllAndReturnCallbackMessage();

        sender.sendMessage(callbackMessage);
    }

    private String colored(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
