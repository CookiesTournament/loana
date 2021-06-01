package org.cookiesturnier.loana.tournament.utils;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.log4j.Level;
import org.cookiesturnier.loana.tournament.TournamentManager;
import org.cookiesturnier.loana.tournament.database.objects.Key;
import org.cookiesturnier.loana.tournament.objects.Player;
import org.cookiesturnier.loana.tournament.objects.Team;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 01.06.2021
 * Time: 18:52
 * Project: loana
 */

@Getter
public class TeamManager {

    private final List<Team> registeredTeams;
    private final List<Player> loadedPlayers;

    public TeamManager() {
        this.registeredTeams = Lists.newCopyOnWriteArrayList();
        this.loadedPlayers = Lists.newCopyOnWriteArrayList();

        try {
            TournamentManager.getInstance().getLogger().log(Level.INFO, "Loading data from database..");
            this.loadPlayersFromDatabase();
            this.loadTeamsFromDatabase();
            TournamentManager.getInstance().getLogger().log(Level.INFO, "Loading finished. Success.");
        } catch (SQLException | JSONException exception) {
            TournamentManager.getInstance().getLogger().log(Level.ERROR, "An error occurred while loading the data from the database!", exception);
        }
    }

    public Team registerTeam(String displayName, List<Player> members) {
        final UUID uuid = this.findUnusedUUID("teams");
        final Team team = new Team(uuid, displayName, members);

        try {
            team.saveToDatabase();
        } catch (IOException | SQLException exception) {
            TournamentManager.getInstance().getLogger().log(Level.ERROR, "An error occurred while saving the team to the database.", exception);
        }
        this.registeredTeams.add(team);

        return team;
    }

    public Player registerPlayer(UUID uuid, String customName, String discordTag) {
        final Player player = new Player(uuid, customName, discordTag);
        player.saveToDatabase();
        this.loadedPlayers.add(player);
        return player;
    }

    private void loadPlayersFromDatabase() throws SQLException {
        final ResultSet resultSet = TournamentManager.getInstance().getDatabaseAdapter().getResultSet("SELECT * FROM players");

        while (resultSet.next()) {
            final Player player = new Player(
                    UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getString("customName"),
                    resultSet.getString("discordTag"));

            this.loadedPlayers.add(player);
            TournamentManager.getInstance().getLogger().log(Level.DEBUG, "Player " + player.getUuid() + " has been loaded successfully.");
        }
    }

    private void loadTeamsFromDatabase() throws SQLException, JSONException {
        final ResultSet resultSet = TournamentManager.getInstance().getDatabaseAdapter().getResultSet("SELECT * FROM teams");

        while (resultSet.next()) {
            final JSONArray jsonArray = new JSONArray(resultSet.getString("members"));
            final List<Player> members = Lists.newCopyOnWriteArrayList();

            for(int i = 0; i<jsonArray.length(); i++) {
                final UUID uuid = UUID.fromString(jsonArray.getString(i));
                final Player player = this.loadedPlayers.stream().filter(player1 -> player1.getUuid().equals(uuid)).findAny().orElse(null);

                if(player != null)
                    members.add(player);
                else
                    TournamentManager.getInstance().getLogger().log(Level.ERROR, "Couldn't find a player with the uuid " + uuid);
            }

            final Team team = new Team(UUID.fromString(resultSet.getString("uuid")), resultSet.getString("name"), members);
            this.registeredTeams.add(team);
            TournamentManager.getInstance().getLogger().log(Level.DEBUG, "Team " + team.getUuid() + " has been loaded successfully.");
        }
    }


    private UUID findUnusedUUID(String table) {
        UUID uuid = UUID.randomUUID();

        while (TournamentManager.getInstance().getDatabaseAdapter().containsEntry(table, new Key("uuid", uuid.toString()))) {
            TournamentManager.getInstance().getLogger().log(Level.DEBUG, "UUID " + uuid + " already in use! Generating a new one..");
            uuid = UUID.randomUUID();
        }

        return uuid;
    }

}
