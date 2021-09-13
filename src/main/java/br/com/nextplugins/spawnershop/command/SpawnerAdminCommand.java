package br.com.nextplugins.spawnershop.command;

import br.com.nextplugins.spawnershop.configuration.ConfigurationManager;
import br.com.nextplugins.spawnershop.configuration.value.MessageValue;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

@RequiredArgsConstructor
public final class SpawnerAdminCommand {

    private final ConfigurationManager configurationManager;

    @Command(
        name = "spawneradmin",
        description = "Comandos administrativos do sistema.",
        permission = "nextspawnershop.command.spawneradmin",
        async = true
    )
    public void handleAdmin(Context<CommandSender> context) {
        final CommandSender sender = context.getSender();

        final List<String> messages = MessageValue.get(MessageValue::staffCommandHelp);

        for (String message : messages) {
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
