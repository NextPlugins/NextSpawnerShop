package br.com.nextplugins.spawnershop.database.repository.adapter;

import br.com.nextplugins.spawnershop.api.model.user.User;
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import org.bukkit.Bukkit;

public final class UserAdapter implements SQLResultAdapter<User> {

    @Override
    public User adaptResult(SimpleResultSet resultSet) {
        return User.builder()
            .player(Bukkit.getPlayer(String.valueOf(resultSet.get("player"))))
            .limit(Double.parseDouble(resultSet.get("limit")))
            .boughtSpawners(Double.parseDouble(resultSet.get("boughtSpawners")))
            .build();
    }

}
