package br.com.nextplugins.spawnershop.database.repository;

import br.com.nextplugins.spawnershop.api.model.user.User;
import br.com.nextplugins.spawnershop.database.repository.adapter.UserAdapter;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Set;

@RequiredArgsConstructor
public final class UserRepository {

    private final String TABLE = "nextspawnershop_data";

    private final SQLExecutor executor;

    public void createTable() {
        executor.updateQuery("CREATE TABLE IF NOT EXISTS " + TABLE + "(" +
            "player VARCHAR(16) NOT NULL PRIMARY KEY," +
            "limit DOUBLE NOT NULL DEFAULT 0," +
            "boughtSpawners DOUBLE NOT NULL DEFAULT 0" +
            ");");
    }

    public User selectOne(String query) {
        if (query == null) query = "";

        return executor.resultOneQuery(
            String.format("SELECT * FROM `%s` %s", TABLE, query),
            $ -> {
            },
            UserAdapter.class
        );
    }

    public User selectOne(Player player) {
        return selectOne("WHERE `player` = `" + player.getName() + "`");
    }

    public Set<User> selectAll(String query) {
        if (query == null) query = "";

        return executor.resultManyQuery(
            String.format("SELECT * FROM `%s` %s", TABLE, query),
            $ -> {
            },
            UserAdapter.class
        );
    }

    public Set<User> selectAll() {
        return selectAll(null);
    }

    public void saveOne(User user) {
        executor.updateQuery(
            String.format("REPLACE INTO `%s` VALUES(?,?,?)", TABLE),
            statement -> {
                statement.set(1, user.getPlayer().getName());
                statement.set(2, user.getLimit());
                statement.set(3, user.getBoughtSpawners());
            }
        );
    }

}
