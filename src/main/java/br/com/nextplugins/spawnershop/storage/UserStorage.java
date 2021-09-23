package br.com.nextplugins.spawnershop.storage;

import br.com.nextplugins.spawnershop.api.model.user.User;
import br.com.nextplugins.spawnershop.database.repository.UserRepository;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public final class UserStorage {

    private final UserRepository userRepository;

    @Getter private final AsyncLoadingCache<String, User> cache = Caffeine.newBuilder()
        .maximumSize(1000)
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .evictionListener((RemovalListener<String, User>) (key, value, cause) -> userRepository.saveOne(value))
        .removalListener((key, value, cause) -> userRepository.saveOne(value))
        .buildAsync((key, executor) -> CompletableFuture.completedFuture(userRepository.selectOne(key)));

    public void init() {
        userRepository.createTable();
    }

    public User findUserByPlayerName(String playerName) {
        try {
            return cache.get(playerName).get();
        } catch (InterruptedException | ExecutionException exception) {
            Thread.currentThread().interrupt();
            exception.printStackTrace();
            return null;
        }
    }

    public User findUser(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            final Player player = offlinePlayer.getPlayer();

            if (player != null) return findUser(player);
        }

        return findUserByPlayerName(offlinePlayer.getName());
    }

    public User findUser(Player player) {
        User user = findUserByPlayerName(player.getName());

        if (user == null) {
            user = User.builder()
                .player(player)
                .limit(0)
                .boughtSpawners(0)
                .build();

            put(user);
        }

        return user;
    }

    public void put(User user) {
        cache.put(user.getPlayer().getName(), CompletableFuture.completedFuture(user));
    }

}
