package org.cookiesturnier.loana.tournament;

import lombok.Getter;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.cookiesturnier.loana.NodeApplication;
import org.cookiesturnier.loana.tournament.config.ConfigLoader;
import org.cookiesturnier.loana.tournament.database.Database;
import org.cookiesturnier.loana.tournament.database.DatabaseAdapter;
import org.springframework.boot.SpringApplication;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 29.05.2021
 * Time: 22:26
 * Project: loana
 */

@Getter
public class TournamentManager {

    @Getter
    private static TournamentManager instance;
    private static final String VERSION = "v1.0";
    private Logger logger;

    private ConfigLoader configLoader;
    private Database database;
    private DatabaseAdapter databaseAdapter;

    public TournamentManager() {
        instance = this;
        this.setupLogger();
        this.printHeader();

        this.loadConfig();
        this.initDatabase();

        //new Thread(this::initSpringFramework).start();
    }

    private void setupLogger() {
        this.logger = Logger.getLogger(TournamentManager.class);

        final ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setThreshold(Level.INFO);
        consoleAppender.setLayout(new PatternLayout("%d [%p|%c|%C{1}] %m%n"));
        consoleAppender.activateOptions();
        this.logger.addAppender(consoleAppender);
    }

    private void printHeader() {
        this.logger.log(Level.DEBUG, "Starting management backend..");
        System.out.println("_________                __   .__                ___________                  .__              \n" +
                "\\_   ___ \\  ____   ____ |  | _|__| ____   ______ \\__    ___/_ _________  ____ |__| ___________ \n" +
                "/    \\  \\/ /  _ \\ /  _ \\|  |/ /  |/ __ \\ /  ___/   |    | |  |  \\_  __ \\/    \\|  |/ __ \\_  __ \\\n" +
                "\\     \\___(  <_> |  <_> )    <|  \\  ___/ \\___ \\    |    | |  |  /|  | \\/   |  \\  \\  ___/|  | \\/\n" +
                " \\______  /\\____/ \\____/|__|_ \\__|\\___  >____  >   |____| |____/ |__|  |___|  /__|\\___  >__|   \n" +
                "        \\/                   \\/       \\/     \\/                             \\/        \\/       ");

        System.out.println("Cookies-Turnier by VoxCrafter_LP & Lezurex (" + VERSION + ")");

        for (int i = 0; i<3; i++)
            System.out.println(" ");
    }

    private void loadConfig() {
        this.configLoader = new ConfigLoader();
    }

    private void initDatabase() {
        this.logger.log(Level.DEBUG, "Initializing database..");
        this.database = new Database(this.configLoader.getConfig().getDatabaseHost(),
                this.configLoader.getConfig().getDatabaseUser(),
                this.configLoader.getConfig().getDatabasePassword(),
                this.configLoader.getConfig().getDatabaseName(),
                this.configLoader.getConfig().getDatabasePort());
        this.database.connect();
        this.databaseAdapter = new DatabaseAdapter(this.database);
    }

    private void initSpringFramework() {
        SpringApplication.run(NodeApplication.class);
    }

}
