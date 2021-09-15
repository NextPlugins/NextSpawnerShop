package br.com.nextplugins.spawnershop.listener;

import br.com.nextplugins.spawnershop.NextSpawnerShop;
import br.com.nextplugins.spawnershop.api.event.SpawnerCustomAmountBuyEvent;
import br.com.nextplugins.spawnershop.api.model.spawner.Spawner;
import br.com.nextplugins.spawnershop.configuration.value.MessageValue;
import br.com.nextplugins.spawnershop.manager.SpawnerManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public final class PlayerChatListener implements Listener {

    private final SpawnerManager spawnerManager;

    @EventHandler
    public void handlePlayerChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();

        final Map<UUID, Spawner> playersBuyingSpawners = spawnerManager.getPlayersBuyingSpawners();

        if (playersBuyingSpawners.containsKey(player.getUniqueId())) {
            event.setCancelled(true);

            double amount;

            try {
                amount = Double.parseDouble(event.getMessage());
            } catch (Throwable t) {
                player.sendMessage(MessageValue.get(MessageValue::onlyNumbers));
                return;
            }

            final Spawner spawner = playersBuyingSpawners.get(player.getUniqueId());

            Bukkit.getScheduler().runTask(NextSpawnerShop.getInstance(), () -> {
                final SpawnerCustomAmountBuyEvent spawnerCustomAmountBuyEvent = new SpawnerCustomAmountBuyEvent(
                    player,
                    amount,
                    spawner
                );

                Bukkit.getPluginManager().callEvent(spawnerCustomAmountBuyEvent);
            });

            playersBuyingSpawners.remove(player.getUniqueId());
        }
    }

}
