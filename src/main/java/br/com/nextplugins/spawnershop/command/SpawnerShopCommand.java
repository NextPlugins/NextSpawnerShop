package br.com.nextplugins.spawnershop.command;

import br.com.nextplugins.spawnershop.hook.EconomyHook;
import br.com.nextplugins.spawnershop.manager.DiscountManager;
import br.com.nextplugins.spawnershop.manager.SpawnerManager;
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

}
