package br.com.nextplugins.spawnershop.api.model.spawner.data;

import lombok.Builder;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
@Builder
public final class SpawnerData {

    private final double cost, cashCost;

    private final ItemStack icon;

    private final List<String> commands;

}
