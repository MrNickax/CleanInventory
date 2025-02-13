package com.nickax.cleaninventory.storage;

import com.nickax.cleaninventory.config.MainConfig;
import com.nickax.cleaninventory.data.PlayerData;
import com.nickax.genten.credential.DatabaseCredential;
import com.nickax.genten.credential.MongoCredential;
import com.nickax.genten.credential.MySQLCredential;
import com.nickax.genten.repository.JsonRepository;
import com.nickax.genten.repository.MongoRepository;
import com.nickax.genten.repository.MySQLRepository;
import com.nickax.genten.repository.Repository;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

public class StorageLoader {

    public static Repository<UUID, PlayerData> load(JavaPlugin plugin, MainConfig config) {
        DatabaseCredential credential = config.getDatabaseCredential();
        return credential != null
                ? load(credential)
                : new JsonRepository<>(new File(plugin.getDataFolder(), "data/player"), PlayerData.class);
    }

    public static Repository<UUID, PlayerData> load(DatabaseCredential credential) {
        return credential instanceof MongoCredential
                ? new MongoRepository<>((MongoCredential) credential, PlayerData.class)
                : new MySQLRepository<>((MySQLCredential) credential, PlayerData.class);
    }
}
