package br.com.nextplugins.spawnershop.view;

import br.com.nextplugins.spawnershop.api.event.SpawnerBuyEvent;
import br.com.nextplugins.spawnershop.api.event.SpawnerCustomAmountBuyEvent;
import br.com.nextplugins.spawnershop.api.model.discount.Discount;
import br.com.nextplugins.spawnershop.api.model.spawner.Spawner;
import br.com.nextplugins.spawnershop.configuration.value.MessageValue;
import br.com.nextplugins.spawnershop.hook.EconomyHook;
import br.com.nextplugins.spawnershop.manager.DiscountManager;
import br.com.nextplugins.spawnershop.manager.SpawnerManager;
import br.com.nextplugins.spawnershop.util.item.ItemBuilder;
import br.com.nextplugins.spawnershop.util.item.TypeUtil;
import br.com.nextplugins.spawnershop.util.time.TimeFormatter;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.border.Border;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.impl.ViewerConfigurationImpl;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.PluginManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class SpawnerShopView extends PagedInventory {

    private final SpawnerManager spawnerManager;
    private final DiscountManager discountManager;
    private final EconomyHook economyHook;

    private final PluginManager pluginManager = Bukkit.getPluginManager();

    public SpawnerShopView(SpawnerManager spawnerManager, DiscountManager discountManager, EconomyHook economyHook) {
        super(
            "nextspawnershop.view.main",
            "Spawners",
            6 * 9
        );

        configuration(configuration -> configuration.secondUpdate(1));

        this.spawnerManager = spawnerManager;
        this.discountManager = discountManager;
        this.economyHook = economyHook;
    }

    @Override
    protected void configureViewer(PagedViewer viewer) {
        final ViewerConfigurationImpl.Paged configuration = viewer.getConfiguration();

        configuration.border(
            Border.of(2, 1, 1, 1)
        );
    }

    @Override
    protected List<InventoryItemSupplier> createPageItems(PagedViewer viewer) {
        final List<InventoryItemSupplier> items = new LinkedList<>();

        final Player player = viewer.getPlayer();

        for (Spawner spawner : spawnerManager.getSpawners().values()) {
            items.add(() -> {
                InventoryItem inventoryItem;

                if (spawner.isReleased()) {
                    final Discount discount = discountManager.getPlayerDiscount(player).orElse(null);

                    final double cost = spawner.getData().getCost();
                    final double cashCost = spawner.getData().getCashCost();

                    inventoryItem = InventoryItem.of(spawner.getData().toItemStack(discount, discountManager))
                        .callback(ClickType.LEFT, handler -> {
                            final SpawnerBuyEvent spawnerBuyEvent = new SpawnerBuyEvent(player, spawner);

                            pluginManager.callEvent(spawnerBuyEvent);
                        })
                        .callback(ClickType.RIGHT, handler -> {
                            player.closeInventory();

                            spawnerManager.getPlayersBuyingSpawners().put(player.getUniqueId(), spawner);

                            final List<String> messages = MessageValue.get(MessageValue::customAmountBuyMessage);

                            for (String message : messages) {
                                player.sendMessage(message);
                            }
                        })
                        .callback(ClickType.DROP, handler -> {
                            final double finalCost = discount == null
                                ? cost
                                : discountManager.getSpawnerPriceWithDiscount(cost, discount.getValue());

                            // todo: apply cash cost to spawner amount

                            final double finalCashCost = discount == null
                                ? cashCost
                                : discountManager.getSpawnerPriceWithDiscount(cashCost, discount.getValue());

                            final double balance = economyHook.getBalance(player);

                            final double spawnerAmount = Math.floor(balance / finalCost);

                            final SpawnerCustomAmountBuyEvent spawnerCustomAmountBuyEvent = new SpawnerCustomAmountBuyEvent(
                                player,
                                spawnerAmount,
                                spawner
                            );

                            pluginManager.callEvent(spawnerCustomAmountBuyEvent);
                        });
                } else {
                    inventoryItem = InventoryItem.of(
                        new ItemBuilder(Objects.requireNonNull(TypeUtil.convertFromLegacy("SKULL_ITEM", 3)))
                            .skullTexture("9ddca93d416f5537a827aaffd5e4c5adf9a8b2aef840dbd333861e2c5f91228c")
                            .name("&cEm breve...")
                            .lore(
                                "",
                                " &fPreço:&7 ???",
                                "",
                                "&cEste gerador será lançado em:",
                                "&c" + TimeFormatter.format(spawner.remainingTimeToRelease())
                            )
                            .build()
                    );
                }

                return inventoryItem;
            });
        }

        return items;
    }

}
