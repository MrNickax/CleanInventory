package com.nickax.cleaninventory.credential;

import com.nickax.genten.config.ConfigSection;
import com.nickax.genten.credential.DatabaseCredential;
import com.nickax.genten.credential.MongoCredential;
import com.nickax.genten.credential.MySQLCredential;

public class DatabaseCredentialLoader {

    private static final String DEFAULT_MONGO_PORT = "27017";
    private static final String DEFAULT_MYSQL_PORT = "3306";
    private static final String DEFAULT_COLLECTION = "cleaninventory";

    public static DatabaseCredential load(ConfigSection databaseSection) {
        String type = databaseSection.castValue("type", String.class);

        String[] address = parseAddress(type, databaseSection.castValue("address", String.class));
        String database = databaseSection.castValue("database", String.class);
        String username = databaseSection.castValue("username", String.class);
        String password = databaseSection.castValue("password", String.class);

        return createCredential(type, address[0], address[1], database, username, password);
    }

    private static String[] parseAddress(String type, String address) {
        String[] parts = address.split(":");
        String host = parts[0];
        String port = (parts.length > 1) ? parts[1] : getDefaultPort(type);
        return new String[]{host, port};
    }

    private static String getDefaultPort(String type) {
        return type.equalsIgnoreCase("MONGODB") ? DEFAULT_MONGO_PORT : DEFAULT_MYSQL_PORT;
    }

    private static DatabaseCredential createCredential(String type, String host, String port, String database, String username, String password) {
        switch (type) {
            case "MONGODB":
                return new MongoCredential(host, port, database, DEFAULT_COLLECTION, username, password);
            case "MYSQL":
                return new MySQLCredential(host, port, database, DEFAULT_COLLECTION, username, password);
            default:
                return null;
        }
    }
}
