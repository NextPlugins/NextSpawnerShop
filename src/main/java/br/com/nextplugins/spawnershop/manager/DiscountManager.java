package br.com.nextplugins.spawnershop.manager;

import br.com.nextplugins.spawnershop.NextSpawnerShop;
import br.com.nextplugins.spawnershop.api.model.discount.Discount;
import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public final class DiscountManager {

    private final NextSpawnerShop plugin;

    @Getter private final Map<String, Discount> discounts = new LinkedHashMap<>();

    public void init() {
        loadDiscounts(false);
    }

    public void loadDiscounts(boolean fromReload) {
        final Stopwatch timer = Stopwatch.createStarted();

        if (fromReload) discounts.clear();

        final FileConfiguration configuration = plugin.getConfigurationManager().getDiscountsConfiguration();
        final ConfigurationSection discountsSection = configuration.getConfigurationSection("discounts");

        if (discountsSection == null) return;

        final Set<String> keys = discountsSection.getKeys(false);

        for (String key : keys) {
            discounts.put(discountsSection.getString(key + ".permission"),
                Discount.builder()
                    .id(key)
                    .permission(discountsSection.getString(key + ".permission"))
                    .value(discountsSection.getDouble(key + ".value"))
                    .groupName(discountsSection.getString(key + ".group-name"))
                    .build()
            );
        }

        timer.stop();

        if (!fromReload) {
            final Logger logger = plugin.getLogger();

            logger.log(Level.INFO, "Todos os descontos foram carregados e cacheados. ({0}ms)", timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public double getSpawnerPriceWithDiscount(double cost, double discount) {
        return cost - Math.floor(cost * (discount / 100));
    }

    public Optional<Discount> getPlayerDiscount(Player player) {
        Discount discount = null;

        for (String permission : discounts.keySet()) {
            if (player.hasPermission(permission)) {
                discount = discounts.get(permission);
            }
        }

        return Optional.ofNullable(discount);
    }

}
