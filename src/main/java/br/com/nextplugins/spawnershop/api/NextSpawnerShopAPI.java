package br.com.nextplugins.spawnershop.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NextSpawnerShopAPI {

    @Getter private static final NextSpawnerShopAPI instance = new NextSpawnerShopAPI();

}
