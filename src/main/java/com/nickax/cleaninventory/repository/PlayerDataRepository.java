package com.nickax.cleaninventory.repository;

import com.nickax.cleaninventory.data.PlayerData;
import com.nickax.genten.repository.Repository;
import com.nickax.genten.repository.dual.DualRepository;
import com.nickax.genten.repository.dual.TargetRepository;

import java.util.UUID;

public class PlayerDataRepository extends DualRepository<UUID, PlayerData> {

    public PlayerDataRepository(Repository<UUID, PlayerData> cache, Repository<UUID, PlayerData> storage) {
        super(cache, storage);
    }

    public void loadFromStorageToCache(UUID key, PlayerData defaultPlayerData) {
        PlayerData playerData = get(key, TargetRepository.TWO);
        put(
                key,
                playerData != null
                        ? playerData
                        : defaultPlayerData,
                TargetRepository.ONE
        );
    }

    public void saveFromCacheToStorage(UUID key) {
        PlayerData playerData = get(key, TargetRepository.ONE);
        if (playerData != null) {
            put(key, playerData, TargetRepository.TWO);
        }
    }
}
