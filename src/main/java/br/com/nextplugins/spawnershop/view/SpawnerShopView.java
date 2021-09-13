package br.com.nextplugins.spawnershop.view;

import br.com.nextplugins.spawnershop.api.event.SpawnerBuyEvent;
import br.com.nextplugins.spawnershop.api.model.discount.Discount;
import br.com.nextplugins.spawnershop.api.model.spawner.Spawner;
import br.com.nextplugins.spawnershop.hook.EconomyHook;
import br.com.nextplugins.spawnershop.manager.DiscountManager;
import br.com.nextplugins.spawnershop.manager.SpawnerManager;
import br.com.nextplugins.spawnershop.util.ColorUtil;
import br.com.nextplugins.spawnershop.util.NumberFormatter;
import br.com.nextplugins.spawnershop.util.item.ItemBuilder;
import br.com.nextplugins.spawnershop.util.item.TypeUtil;
import br.com.nextplugins.spawnershop.util.time.TimeFormatter;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        this.spawnerManager = spawnerManager;
        this.discountManager = discountManager;
        this.economyHook = economyHook;
    }

    // todo: refactor this code block

    @Override
    protected List<InventoryItemSupplier> createPageItems(PagedViewer viewer) {
        final List<InventoryItemSupplier> items = new LinkedList<>();

        final Player player = viewer.getPlayer();

        for (Spawner spawner : spawnerManager.getSpawners().values()) {
            items.add(() -> {
                InventoryItem inventoryItem;

                if (spawner.isReleased()) {
                    final ItemStack spawnerIcon = spawner.getData().getIcon().clone();

                    final Discount discount = discountManager.getPlayerDiscount(player).orElse(null);

                    final double cost = spawner.getData().getCost();
                    final double cashCost = spawner.getData().getCashCost();

                    final ItemStack icon = new ItemBuilder(spawnerIcon)
                        .acceptItemMeta(meta -> {
                            final List<String> lore = meta.getLore();

                            if (lore == null) return;

                            final String formattedPrice = NumberFormatter.letterFormat(cost);

                            final String coinsPrice = discount == null
                                ? formattedPrice
                                : ColorUtil.colored("&c&m" + formattedPrice + "&r" + "&a " + NumberFormatter.letterFormat(
                                discountManager.getSpawnerPriceWithDiscount(cost, discount.getValue())
                            ));

                            final String formattedCashPrice = NumberFormatter.letterFormat(cashCost);

                            final String cashPrice = discount == null
                                ? formattedCashPrice
                                : ColorUtil.colored("&c&m" + formattedCashPrice + "&r" + "&6 " + NumberFormatter.letterFormat(
                                discountManager.getSpawnerPriceWithDiscount(cashCost, discount.getValue())
                            ));

                            final String discountLine = discount == null
                                ? ChatColor.RED + "Você não possui nenhum desconto."
                                : NumberFormatter.decimalFormat(discount.getValue()) + "% - " + ColorUtil.colored(discount.getGroupName());

                            final List<String> replacedLore = lore.stream()
                                .map(s -> s.replace("{coins}", coinsPrice))
                                .map(s -> s.replace("{cash}", cashPrice))
                                .map(s -> s.replace("{discount}", discountLine))
                                .collect(Collectors.toList());

                            meta.setLore(replacedLore);
                        })
                        .build();

                    inventoryItem = InventoryItem.of(icon)
                        .callback(ClickType.LEFT, handler -> {
                            final double finalCost = discount == null
                                ? cost
                                : discountManager.getSpawnerPriceWithDiscount(cost, discount.getValue());

                            final SpawnerBuyEvent spawnerBuyEvent = new SpawnerBuyEvent(player, 1, finalCost, spawner);

                            pluginManager.callEvent(spawnerBuyEvent);
                        })
                        .callback(ClickType.RIGHT, handler -> {
                            // todo: handle right click
                        })
                        .callback(ClickType.DROP, handler -> {
                            final double finalCost = discount == null
                                ? cost
                                : discountManager.getSpawnerPriceWithDiscount(cost, discount.getValue());

                            final double balance = economyHook.getBalance(player);

                            final double spawnerAmount = Math.floor(balance / finalCost);

                            final double totalCost = spawnerAmount * finalCost;

                            final SpawnerBuyEvent spawnerBuyEvent = new SpawnerBuyEvent(
                                player,
                                spawnerAmount,
                                totalCost,
                                spawner
                            );

                            pluginManager.callEvent(spawnerBuyEvent);
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
