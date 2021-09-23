package br.com.nextplugins.spawnershop.api.model.user;

import lombok.Builder;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
@Builder
public final class User {

    private final Player player;

    private double limit, boughtSpawners;

    public void increaseBoughtSpawners(double value) {
        this.boughtSpawners += value;
    }

    public void increaseLimit(double value) {
        this.limit += value;
    }

    public boolean hasLimit(double value) {
        return limit >= value;
    }

}
