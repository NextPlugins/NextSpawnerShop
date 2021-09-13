package br.com.nextplugins.spawnershop.util.item;

import br.com.nextplugins.spawnershop.util.ColorUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ItemParser {

    public ItemStack parseSection(ConfigurationSection section) {
        try {
            ItemBuilder itemBuilder;

            if (section.contains("head")) {
                if (Objects.requireNonNull(section.getString("head")).length() > 16) {
                    itemBuilder = new ItemBuilder(Objects.requireNonNull(TypeUtil.convertFromLegacy(
                        "SKULL_ITEM",
                        3
                    )))
                        .skullTexture(section.getString("head"));
                } else {
                    itemBuilder = new ItemBuilder(Objects.requireNonNull(TypeUtil.convertFromLegacy(
                        "SKULL_ITEM",
                        3
                    )))
                        .skullOwner(section.getString("head"));
                }
            } else {
                itemBuilder = new ItemBuilder(Objects.requireNonNull(TypeUtil.convertFromLegacy(
                    section.getString("material"),
                    section.contains("data") ? (short) section.getInt("data") : 0))
                );
            }

            if (section.contains("name")) itemBuilder.name(ColorUtil.colored(section.getString("name")));
            if (section.contains("glow") && section.getBoolean("glow")) itemBuilder.glow();

            if (section.contains("lore")) {

                final List<String> lore = new ArrayList<>();
                for (String description : section.getStringList("lore")) {
                    lore.add(ColorUtil.colored(description));
                }

                itemBuilder.lore(lore);
            }

            return itemBuilder.build();
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            return null;
        }
    }

}
