package br.com.nextplugins.spawnershop;

import br.com.nextplugins.spawnershop.command.manager.CommandManager;
import br.com.nextplugins.spawnershop.configuration.ConfigurationManager;
import br.com.nextplugins.spawnershop.database.SQLProvider;
import br.com.nextplugins.spawnershop.database.repository.UserRepository;
import br.com.nextplugins.spawnershop.hook.EconomyHook;
import br.com.nextplugins.spawnershop.listener.manager.ListenerManager;
import br.com.nextplugins.spawnershop.manager.DiscountManager;
import br.com.nextplugins.spawnershop.manager.SpawnerManager;
import br.com.nextplugins.spawnershop.storage.UserStorage;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class NextSpawnerShop extends JavaPlugin {

    private SQLConnector sqlConnector;
    private SQLExecutor sqlExecutor;

    private UserRepository userRepository;
    private UserStorage userStorage;

    private final ConfigurationManager configurationManager = new ConfigurationManager(this);

    private final SpawnerManager spawnerManager = new SpawnerManager(this);
    private final DiscountManager discountManager = new DiscountManager(this);

    private final EconomyHook economyHook = new EconomyHook(this.getLogger());

    @Override
    public void onEnable() {
        try {
            sqlConnector = SQLProvider.of(this).setup(null);
            sqlExecutor = new SQLExecutor(sqlConnector);

            configurationManager.init();
            discountManager.init();
            spawnerManager.init();

            userRepository = new UserRepository(sqlExecutor);
            userStorage = new UserStorage(userRepository);

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
