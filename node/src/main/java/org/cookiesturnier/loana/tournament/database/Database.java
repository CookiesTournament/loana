package org.cookiesturnier.loana.tournament.database;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Level;
import org.cookiesturnier.loana.tournament.TournamentManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 02.12.2020
 * Time: 10:31
 * Project: Pets
 */

@Getter
@Slf4j
public class Database {

    private final String host;
    private final String user;
    private final String password;
    private final int port;
    private final String database;
    private Connection connection;

    public Database(String host, String username, String password, String database, int port) {
        this.host = host;
        this.user = username;
        this.password = password;
        this.port = port;
        this.database = database;
    }

    /**
     * Connects to the database
     */
    public void connect() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
            log.info("Successfully connected to the database");
        } catch (SQLException exception) {
            log.error("An error occurred while connecting to the database", exception);
        }
    }

    /**
     * Disconnects from the database
     */
    public void disconnect() {
        if(this.isConnected()) {
            try {
                this.connection.close();
            } catch (SQLException exception) {
                log.error("An error occurred while disconnecting from the database", exception);
            }
        }
    }

    /**
     * @return Returns if the connection has been established
     */
    public boolean isConnected() {
        return this.connection != null;
    }

}