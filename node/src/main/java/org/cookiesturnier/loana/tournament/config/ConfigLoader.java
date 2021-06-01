package org.cookiesturnier.loana.tournament.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import org.apache.log4j.Level;
import org.cookiesturnier.loana.tournament.TournamentManager;

import java.io.*;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 31.05.2021
 * Time: 20:38
 * Project: loana
 */

@Getter
public class ConfigLoader {

    private final File configFile;
    private Config config;

    public ConfigLoader() {
        this.configFile = new File("config.json");
        try {
            TournamentManager.getInstance().getLogger().log(Level.DEBUG, "Loading config file");
            this.loadConfigFromFile();
            TournamentManager.getInstance().getLogger().log(Level.INFO, "The config has been loaded successfully");
        } catch (IOException exception) {
            TournamentManager.getInstance().getLogger().log(Level.ERROR, "An error occurred while loading the config file.", exception);
        }
    }

    private void loadConfigFromFile() throws IOException {
        if(!this.configFile.exists()) {
            this.config = new Config();

            this.config.setDatabaseHost("localhost");
            this.config.setDatabaseUser("root");
            this.config.setDatabasePassword("root");
            this.config.setDatabaseName("tournament");
            this.config.setDatabasePort(3306);
            this.config.setStreamKey("abc");

            this.updateConfig();
            return;
        }

        this.config = new Gson().fromJson(new JsonReader(new FileReader(this.configFile)), Config.class);
    }

    public void updateConfig() throws IOException {
        try(final Writer writer = new FileWriter(this.configFile)) {
            new GsonBuilder().create().toJson(this.config, writer);
            TournamentManager.getInstance().getLogger().log(Level.DEBUG, "Config file updated");
        } catch (Exception exception) {
            TournamentManager.getInstance().getLogger().log(Level.ERROR, "An error occurred while saving the config file.", exception);
        }
    }

}
