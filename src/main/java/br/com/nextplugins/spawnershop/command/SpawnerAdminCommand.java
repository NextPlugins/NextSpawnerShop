package br.com.nextplugins.spawnershop.command;

import br.com.nextplugins.spawnershop.api.model.user.User;
import br.com.nextplugins.spawnershop.configuration.ConfigurationManager;
import br.com.nextplugins.spawnershop.configuration.value.MessageValue;
import br.com.nextplugins.spawnershop.storage.UserStorage;
import br.com.nextplugins.spawnershop.util.NumberFormatter;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public final class SpawnerAdminCommand {

    private final ConfigurationManager configurationManager;
    private final UserStorage storage;

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

    @Command(
        name = "spawneradmin.limit",
        aliases = {"limite"},
        description = "Veja os comandos relacionados ao sistema de limite.",
        permission = "nextspawnershop.command.spawneradmin.limit",
        async = true
    )
    public void handleLimitGive(Context<CommandSender> context) {
        final CommandSender sender = context.getSender();

        final List<String> messages = MessageValue.get(MessageValue::limitCommandHelp);

        for (String message : messages) {
            sender.sendMessage(colored(message));
        }
    }

    @Command(
        name = "spawneradmin.limit.dar",
        aliases = {"give"},
        description = "Adicione limite para alguém.",
        permission = "nextspawnershop.command.spawneradmin.limit.give",
        usage = "spawneradmin limite give (quantidade) (player)",
        async = true
    )
    public void handleLimitGive(Context<CommandSender> context, double amount, @Optional Player target) {
        final CommandSender sender = context.getSender();

        if (amount <= 0) {
            sender.sendMessage(MessageValue.get(MessageValue::wrongAmount));
            return;
        }

        if (target == null) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(MessageValue.get(MessageValue::consoleNotAllowed));
                return;
            }

            final Player player = (Player) sender;

            final User user = storage.findUser(player);

            user.increaseLimit(amount);

            player.sendMessage(MessageValue.get(MessageValue::giveAmount)
                .replace("{amount}", NumberFormatter.letterFormat(amount))
            );
        } else {
            final User user = this.storage.findUser(target);

            user.increaseLimit(amount);

            sender.sendMessage(MessageValue.get(MessageValue::giveTargetAmount)
                .replace("{amount}", NumberFormatter.letterFormat(amount))
                .replace("{amount}", target.getName())
            );
        }
    }

    @Command(
        name = "spawneradmin.limit.set",
        aliases = {"alterar"},
        description = "Altere o limite de alguém.",
        permission = "nextspawnershop.command.spawneradmin.limit.set",
        usage = "spawneradmin limite set (quantidade) (player)",
        async = true
    )
    public void handleLimitSet(Context<CommandSender> context, double amount, @Optional Player target) {
        final CommandSender sender = context.getSender();

        if (amount <= 0) {
            sender.sendMessage(MessageValue.get(MessageValue::wrongAmount));
            return;
        }

        if (target == null) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(MessageValue.get(MessageValue::consoleNotAllowed));
                return;
            }

            final Player player = (Player) sender;

            final User user = storage.findUser(player);

            user.setLimit(amount);

            player.sendMessage(MessageValue.get(MessageValue::setAmount)
                .replace("{amount}", NumberFormatter.letterFormat(amount))
            );
        } else {
            final User user = this.storage.findUser(target);

            user.setLimit(amount);

            sender.sendMessage(MessageValue.get(MessageValue::setTargetAmount)
                .replace("{player}", target.getName())
                .replace("{amount}", NumberFormatter.letterFormat(amount))
            );
        }
    }

    @Command(
        name = "spawneradmin.limit.remove",
        aliases = {"retirar", "remover"},
        description = "Remova uma quantia de limite de alguém.",
        permission = "nextspawnershop.command.spawneradmin.limit.remove",
        usage = "spawneradmin limite remove (quantidade) (player)",
        async = true
    )
    public void handleLimitRemove(Context<CommandSender> context, double amount, @Optional Player target) {
        final CommandSender sender = context.getSender();

        if (amount <= 0) {
            sender.sendMessage(MessageValue.get(MessageValue::wrongAmount));
            return;
        }

        if (target == null) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(MessageValue.get(MessageValue::consoleNotAllowed));
                return;
            }

            final Player player = (Player) sender;

            final User user = storage.findUser(player);

            user.decreaseLimit(amount);

            player.sendMessage(MessageValue.get(MessageValue::removeAmount)
                .replace("{amount}", NumberFormatter.letterFormat(amount))
            );
        } else {
            final User user = this.storage.findUser(target);

            user.decreaseLimit(amount);

            sender.sendMessage(MessageValue.get(MessageValue::removeAmount)
                .replace("{player}", target.getName())
                .replace("{amount}", NumberFormatter.letterFormat(amount))
            );
        }
    }

    @Command(
        name = "spawneradmin.limit.reset",
        aliases = {"redefinir", "resetar"},
        description = "Resete o limite de alguém.",
        permission = "nextspawnershop.command.spawneradmin.limit.reset",
        usage = "spawneradmin limite reset (player)",
        async = true
    )
    public void handleLimitReset(Context<CommandSender> context, @Optional Player target) {
        final CommandSender sender = context.getSender();

        if (target == null) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(MessageValue.get(MessageValue::consoleNotAllowed));
                return;
            }

            final Player player = (Player) sender;

            final User user = storage.findUser(player);

            user.setLimit(0);

            player.sendMessage(MessageValue.get(MessageValue::resetAmount));
        } else {
            final User user = this.storage.findUser(target);

            user.setLimit(0);

            sender.sendMessage(MessageValue.get(MessageValue::resetTargetAmount));
        }
    }

    private String colored(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
