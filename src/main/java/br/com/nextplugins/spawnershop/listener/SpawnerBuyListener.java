package br.com.nextplugins.spawnershop.listener;

import br.com.nextplugins.spawnershop.api.event.SpawnerBuyEvent;
import br.com.nextplugins.spawnershop.api.model.spawner.Spawner;
import br.com.nextplugins.spawnershop.hook.EconomyHook;
import br.com.nextplugins.spawnershop.util.ColorUtil;
import br.com.nextplugins.spawnershop.util.NumberFormatter;
import lombok.RequiredArgsConstructor;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public final class SpawnerBuyListener implements Listener {

    private final EconomyHook economyHook;

    @EventHandler
    public void handleSpawnerBuy(SpawnerBuyEvent event) {
        final Player player = event.getPlayer();
        final Spawner spawner = event.getSpawner();
        final double finalPrice = event.getFinalPrice();
        final double amount = event.getAmount();

        System.out.println(finalPrice);

        final EconomyResponse economyResponse = economyHook.withdrawCoins(player, finalPrice);

        if (!economyResponse.transactionSuccess()) {
            event.setCancelled(true);

            player.closeInventory();
            player.sendMessage(ColorUtil.colored(
                "&cVocê não tem dinheiro suficiente para comprar esta quantia de spawners."
            ));

            return;
        }

        player.sendMessage(ColorUtil.colored(
            String.format("&aVocê comprou &f%s&a spawner(s) com sucesso!", NumberFormatter.letterFormat(amount))
        ));

        player.closeInventory();

        for (String command : spawner.getData().getCommands()) {
            final String replacedCommand = command.replace("{player}", player.getName())
                .replace("{amount}", String.valueOf(amount));

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replacedCommand);
        }
    }

}
