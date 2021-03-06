package br.com.nextplugins.spawnershop.listener;

import br.com.nextplugins.spawnershop.api.event.SpawnerBuyEvent;
import br.com.nextplugins.spawnershop.api.model.spawner.Spawner;
import br.com.nextplugins.spawnershop.configuration.value.MessageValue;
import br.com.nextplugins.spawnershop.hook.EconomyHook;
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

        final EconomyResponse economyResponse = economyHook.withdrawCoins(player, spawner.getData().getCost());

        if (!economyResponse.transactionSuccess()) {
            event.setCancelled(true);

            player.closeInventory();
            player.sendMessage(MessageValue.get(MessageValue::withoutMoney));

            return;
        }

        player.sendMessage(
            MessageValue.get(MessageValue::spawnersBought)
                .replace("{amount}", String.valueOf(1))
        );

        player.closeInventory();

        for (String command : spawner.getData().getCommands()) {
            final String replacedCommand = command.replace("{player}", player.getName())
                .replace("{amount}", String.valueOf(1));

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replacedCommand);
        }
    }

}
