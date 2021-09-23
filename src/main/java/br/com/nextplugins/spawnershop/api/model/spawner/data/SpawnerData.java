package br.com.nextplugins.spawnershop.api.model.spawner.data;

import br.com.nextplugins.spawnershop.api.model.discount.Discount;
import br.com.nextplugins.spawnershop.manager.DiscountManager;
import br.com.nextplugins.spawnershop.util.ColorUtil;
import br.com.nextplugins.spawnershop.util.NumberFormatter;
import br.com.nextplugins.spawnershop.util.item.ItemBuilder;
import lombok.Builder;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public final class SpawnerData {

    private final double cost, cashCost;

    private final ItemStack icon;

    private final List<String> commands;

    public ItemStack toItemStack(Discount discount, DiscountManager discountManager) {
        return new ItemBuilder(icon)
            .acceptItemMeta(meta -> {
                final List<String> lore = meta.getLore();

                if (lore == null) return;

                final String formattedPrice = NumberFormatter.letterFormat(cost);

                String coinsPrice = discount == null
                    ? formattedPrice
                    : ColorUtil.colored("&c&m" + formattedPrice + "&r" + "&a " + NumberFormatter.letterFormat(
                    discountManager.getSpawnerPriceWithDiscount(cost, discount.getValue())
                ));

                if (cost == 0) coinsPrice = "";

                final String formattedCashPrice = NumberFormatter.letterFormat(cashCost);

                String cashPrice = discount == null
                    ? formattedCashPrice
                    : ColorUtil.colored("&c&m" + formattedCashPrice + "&r" + "&6 " + NumberFormatter.letterFormat(
                    discountManager.getSpawnerPriceWithDiscount(cashCost, discount.getValue())
                ));

                if (cashCost == 0) cashPrice = "";

                final String discountLine = discount == null
                    ? ChatColor.RED + "Você não possui nenhum desconto."
                    : NumberFormatter.decimalFormat(discount.getValue()) + "% - " + ColorUtil.colored(discount.getGroupName());

                final String finalCoinsPrice = coinsPrice;
                final String finalCashPrice = cashPrice;

                final List<String> replacedLore = lore.stream()
                    .map(s -> s.replace("{coins}", finalCoinsPrice))
                    .map(s -> s.replace("{cash}", finalCashPrice))
                    .map(s -> s.replace("{discount}", discountLine))
                    .collect(Collectors.toList());

                meta.setLore(replacedLore);
            }).build();
    }
}
