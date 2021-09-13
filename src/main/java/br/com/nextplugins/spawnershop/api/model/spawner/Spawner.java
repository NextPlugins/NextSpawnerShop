package br.com.nextplugins.spawnershop.api.model.spawner;

import br.com.nextplugins.spawnershop.api.model.spawner.data.SpawnerData;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public final class Spawner {

    private final String id;

    private final SpawnerData data;

    private final Date releaseDate;

    public boolean isReleased() {
        if (releaseDate == null) return true;

        return releaseDate.before(new Date());
    }

    public long remainingTimeToRelease() {
        return releaseDate.getTime() - new Date().getTime();
    }

}