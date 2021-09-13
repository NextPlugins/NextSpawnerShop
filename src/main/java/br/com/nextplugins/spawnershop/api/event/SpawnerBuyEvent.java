package br.com.nextplugins.spawnershop.api.event;

import br.com.nextplugins.spawnershop.api.model.spawner.Spawner;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Data
@EqualsAndHashCode(callSuper = true)
public final class SpawnerBuyEvent extends SpawnerShopEvent implements Cancellable {

    private final Player player;
    private final double amount, finalPrice;
    private final Spawner spawner;

    private boolean cancelled;

}
