package br.com.nextplugins.spawnershop;

import br.com.nextplugins.spawnershop.command.manager.CommandManager;
import br.com.nextplugins.spawnershop.configuration.ConfigurationManager;
import br.com.nextplugins.spawnershop.hook.EconomyHook;
import br.com.nextplugins.spawnershop.listener.manager.ListenerManager;
import br.com.nextplugins.spawnershop.manager.DiscountManager;
import br.com.nextplugins.spawnershop.manager.SpawnerManager;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class NextSpawnerShop extends JavaPlugin {

    private final ConfigurationManager configurationManager = new ConfigurationManager(this);

    private final SpawnerManager spawnerManager = new SpawnerManager(this);
    private final DiscountManager discountManager = new DiscountManager(this);

    private final EconomyHook economyHook = new EconomyHook(this.getLogger());

    @Override
    public void onEnable() {
        try {
            configurationManager.init();
            discountManager.init();
            spawnerManager.init();

            economyHook.init();

            InventoryManager.enable(this);

            CommandManager.of(this).init();
            ListenerManager.of(this).init();

            getLogger().info("Plugin inicializado com sucesso.");
        } catch (Throwable t) {
            t.printStackTrace();
            getLogger().severe("Ocorreu um erro durante a inicialização do plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public static NextSpawnerShop getInstance() {
        return getPlugin(NextSpawnerShop.class);
    }

}
