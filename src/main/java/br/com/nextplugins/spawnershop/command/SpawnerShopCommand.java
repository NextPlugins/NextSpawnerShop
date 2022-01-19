package br.com.nextplugins.spawnershop.command;

import br.com.nextplugins.spawnershop.NextSpawnerShop;
import br.com.nextplugins.spawnershop.api.model.user.User;
import br.com.nextplugins.spawnershop.configuration.value.MessageValue;
import br.com.nextplugins.spawnershop.hook.EconomyHook;
import br.com.nextplugins.spawnershop.manager.DiscountManager;
import br.com.nextplugins.spawnershop.manager.SpawnerManager;
import br.com.nextplugins.spawnershop.util.NumberFormatter;
import br.com.nextplugins.spawnershop.view.SpawnerShopView;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class SpawnerShopCommand {

    private final SpawnerManager spawnerManager;
    private final DiscountManager discountManager;
    private final EconomyHook economyHook;

    @Command(
        name = "spawners",
        aliases = {"spawnershop"},
        description = "Abre a loja de spawners.",
        permission = "nextspawnershop.command.spawners",
        target = CommandTarget.PLAYER,
        async = true
    )
    public void handle(Context<Player> context) {
        final Player player = context.getSender();

        final SpawnerShopView spawnerShopView = new SpawnerShopView(spawnerManager, discountManager, economyHook).init();

        spawnerShopView.openInventory(player);
    }

    @Command(
        name = "spawners.limite",
        aliases = {"limit"},
        description = "Veja o seu limite de compra.",
        permission = "nextspawnershop.command.spawners.limit",
        target = CommandTarget.PLAYER,
        async = true
    )
    public void handleLimit(Context<Player> context) {
        final Player player = context.getSender();

        final User user = NextSpawnerShop.getInstance().getUserStorage().findUser(player);

        final double limit = user.getLimit();

        player.sendMessage(MessageValue.get(MessageValue::viewAmount)
            .replace("{player}", NumberFormatter.letterFormat(limit))
        );
    }

}
