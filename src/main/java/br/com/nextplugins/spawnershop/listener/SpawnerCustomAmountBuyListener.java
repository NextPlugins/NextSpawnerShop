package br.com.nextplugins.spawnershop.listener;

import br.com.nextplugins.spawnershop.api.event.SpawnerCustomAmountBuyEvent;
import br.com.nextplugins.spawnershop.api.model.discount.Discount;
import br.com.nextplugins.spawnershop.api.model.spawner.Spawner;
import br.com.nextplugins.spawnershop.configuration.value.MessageValue;
import br.com.nextplugins.spawnershop.hook.EconomyHook;
import br.com.nextplugins.spawnershop.manager.DiscountManager;
import br.com.nextplugins.spawnershop.util.NumberFormatter;
import lombok.RequiredArgsConstructor;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public final class SpawnerCustomAmountBuyListener implements Listener {

    private final EconomyHook economyHook;
    private final DiscountManager discountManager;

    @EventHandler
    public void handleSpawnerBuy(SpawnerCustomAmountBuyEvent event) {
        final Player player = event.getPlayer();
        final double amount = event.getAmount();
        final Spawner spawner = event.getSpawner();

        final Discount discount = discountManager.getPlayerDiscount(player).orElse(null);

        final double cost = discount == null
            ? spawner.getData().getCost()
            : discountManager.getSpawnerPriceWithDiscount(spawner.getData().getCost(), discount.getValue());

        final EconomyResponse economyResponse = economyHook.withdrawCoins(player, cost);

        if (!economyResponse.transactionSuccess()) {
            event.setCancelled(true);

            player.closeInventory();
            player.sendMessage(MessageValue.get(MessageValue::withoutMoney));

            return;
        }

        player.sendMessage(
            MessageValue.get(MessageValue::spawnersBought)
                .replace("{amount}", NumberFormatter.letterFormat(amount))
        );

        player.closeInventory();

        for (String command : spawner.getData().getCommands()) {
            final String replacedCommand = command.replace("{player}", player.getName())
                .replace("{amount}", String.valueOf(amount));

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replacedCommand);
        }
    }

}
