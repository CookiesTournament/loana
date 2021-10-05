package org.cookiesturnier.loana.tournament;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cookiesturnier.loana.NodeApplication;
import org.cookiesturnier.loana.tournament.api.TournamentAPI;
import org.cookiesturnier.loana.tournament.config.ConfigLoader;
import org.cookiesturnier.loana.tournament.database.Database;
import org.cookiesturnier.loana.tournament.database.DatabaseAdapter;
import org.cookiesturnier.loana.tournament.database.enums.RowType;
import org.cookiesturnier.loana.tournament.database.objects.Row;
import org.cookiesturnier.loana.tournament.utils.TeamManager;
import org.springframework.boot.SpringApplication;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 29.05.2021
 * Time: 22:26
 * Project: loana
 */

@Getter
@Slf4j
public class TournamentManager {

    @Getter
    private static TournamentManager instance;
    private static final String VERSION = "v1.0";

    private ConfigLoader configLoader;
    private Database database;
    private DatabaseAdapter databaseAdapter;
    private final TeamManager teamManager;
    //private final StreamManager streamManager;

    private final TournamentAPI api;

    public TournamentManager() {
        instance = this;
        //this.setupLogger();
        this.printHeader();

        this.loadConfig();
        this.initDatabase();

        this.teamManager = new TeamManager();
        //this.streamManager = new StreamManager(this.getConfigLoader().getConfig().getObsPassword());
        this.api = new TournamentAPI();
        //new Thread(this::initSpringFramework).start();
    }

//    private void setupLogger() {
//        final ConsoleAppender consoleAppender = new ConsoleAppender();
//        consoleAppender.setThreshold(Level.INFO);
//        consoleAppender.setLayout(new PatternLayout("%d [%p|%c|%C{1}] %m%n"));
//        consoleAppender.activateOptions();
//        log.addAppender(consoleAppender);
//    }

    private void printHeader() {
        System.out.println("Starting management backend..");
        System.out.println("_________                __   .__                ___________                  .__              \n" +
                "\\_   ___ \\  ____   ____ |  | _|__| ____   ______ \\__    ___/_ _________  ____ |__| ___________ \n" +
                "/    \\  \\/ /  _ \\ /  _ \\|  |/ /  |/ __ \\ /  ___/   |    | |  |  \\_  __ \\/    \\|  |/ __ \\_  __ \\\n" +
                "\\     \\___(  <_> |  <_> )    <|  \\  ___/ \\___ \\    |    | |  |  /|  | \\/   |  \\  \\  ___/|  | \\/\n" +
                " \\______  /\\____/ \\____/|__|_ \\__|\\___  >____  >   |____| |____/ |__|  |___|  /__|\\___  >__|   \n" +
                "        \\/                   \\/       \\/     \\/                             \\/        \\/       ");

        System.out.println("Cookies-Turnier by VoxCrafter_LP & Lezurex (" + VERSION + ")");
        System.out.println(Strings.repeat(" ", 3));
    }

    private void loadConfig() {
        this.configLoader = new ConfigLoader();
    }

    private void initDatabase() {
        log.debug("Initializing database..");
        this.database = new Database(this.configLoader.getConfig().getDatabaseHost(),
                this.configLoader.getConfig().getDatabaseUser(),
                this.configLoader.getConfig().getDatabasePassword(),
                this.configLoader.getConfig().getDatabaseName(),
                this.configLoader.getConfig().getDatabasePort());
        this.database.connect();
        this.databaseAdapter = new DatabaseAdapter(this.database);

        //CREATE TABLES

        this.databaseAdapter.createTable("teams",
                new Row("uuid", RowType.VARCHAR),
                new Row("name", RowType.VARCHAR),
                new Row("members", RowType.TEXT));
        this.databaseAdapter.createTable("players",
                new Row("uuid", RowType.VARCHAR),
                new Row("customName", RowType.VARCHAR),
                new Row("discordId", RowType.VARCHAR));
    }

    private void initSpringFramework() {
        SpringApplication.run(NodeApplication.class);
    }

}
