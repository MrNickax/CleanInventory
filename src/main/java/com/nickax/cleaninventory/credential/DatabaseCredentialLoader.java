package com.nickax.cleaninventory.credential;

import com.nickax.genten.credential.DatabaseCredential;
import com.nickax.genten.credential.MongoCredential;
import com.nickax.genten.credential.MySQLCredential;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DatabaseCredentialLoader {

    private static final Map<String, Function<ConfigurationSection, DatabaseCredential>> LOADERS = new HashMap<>();

    static {
        LOADERS.put("MONGODB", DatabaseCredentialLoader::loadMongoCredential);
        LOADERS.put("MYSQL", DatabaseCredentialLoader::loadMySQLCredential);
    }

    public static DatabaseCredential load(ConfigurationSection section) {
        String type = section.getString("type");
        Function<ConfigurationSection, DatabaseCredential> loader = LOADERS.get(type);
        return loader != null ? loader.apply(section) : null;
    }

    private static DatabaseCredential loadMongoCredential(ConfigurationSection section) {
        return new MongoCredential(
                section.getString("address").split(":")[0],
                section.getString("address").split(":").length > 1 ? section.getString("port").split(":")[1] : "27017",
                section.getString("database"),
                "cleaninventory",
                section.getString("username"),
                section.getString("password")
        );
    }

    private static DatabaseCredential loadMySQLCredential(ConfigurationSection section) {
        return new MySQLCredential(
                section.getString("address").split(":")[0],
                section.getString("address").split(":").length > 1 ? section.getString("port").split(":")[1] : "3306",
                section.getString("database"),
                "cleaninventory",
                section.getString("username"),
                section.getString("password")
        );
    }
}
