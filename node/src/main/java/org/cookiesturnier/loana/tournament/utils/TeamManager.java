package org.cookiesturnier.loana.tournament.utils;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cookiesturnier.loana.tournament.TournamentManager;
import org.cookiesturnier.loana.tournament.api.exceptions.MojangAPIException;
import org.cookiesturnier.loana.tournament.database.objects.Key;
import org.cookiesturnier.loana.tournament.objects.Player;
import org.cookiesturnier.loana.tournament.objects.Team;
import org.json.JSONArray;
import org.json.JSONException;
import org.shanerx.mojang.Mojang;

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
@Slf4j
public class TeamManager {

    private final List<Team> registeredTeams;
    private final List<Player> loadedPlayers;
    private final Mojang mojangAPI;

    public TeamManager() {
        this.registeredTeams = Lists.newCopyOnWriteArrayList();
        this.loadedPlayers = Lists.newCopyOnWriteArrayList();

        this.mojangAPI = new Mojang().connect();

        try {
            log.info("Loading data from database..");
            this.loadPlayersFromDatabase();
            this.loadTeamsFromDatabase();
            log.info("Loading finished. Success.");
        } catch (SQLException | JSONException exception) {
            log.error("An error occurred while loading the data from the database!", exception);
        }
    }

    public Team registerTeam(String displayName, List<Player> members) {
        final UUID uuid = this.findUnusedUUID("teams");
        final Team team = new Team(uuid, displayName, members);

        try {
            team.saveToDatabase();
            log.info("Team \"" + displayName + "\" (" + members.get(0).getCustomName() + ", " +
                    members.get(1).getCustomName() + ") has registered.");
        } catch (IOException | SQLException exception) {
            log.info("An error occurred while saving the team to the database.", exception);
        }
        this.registeredTeams.add(team);

        return team;
    }

    public Player registerPlayer(UUID uuid, String customName, String discordId) {
        final Player player = new Player(uuid, customName, discordId);
        player.saveToDatabase();
        this.loadedPlayers.add(player);
        return player;
    }

    private void loadPlayersFromDatabase() throws SQLException {
        final ResultSet resultSet = TournamentManager.getInstance().getDatabaseAdapter().getResultSet("SELECT * FROM players");

        while (resultSet.next()) {
            System.out.println(resultSet.getString("uuid"));
            final Player player = new Player(
                    UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getString("customName"),
                    resultSet.getString("discordId"));

            this.loadedPlayers.add(player);
            log.info("Player " + player.getUuid() + " has been loaded successfully.");
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
                    log.error("Couldn't find a player with the uuid " + uuid);
            }

            final Team team = new Team(UUID.fromString(resultSet.getString("uuid")), resultSet.getString("name"), members);
            this.registeredTeams.add(team);
            log.info("Team " + team.getUuid() + " has been loaded successfully.");
        }
    }


    private UUID findUnusedUUID(String table) {
        UUID uuid = UUID.randomUUID();

        while (TournamentManager.getInstance().getDatabaseAdapter().containsEntry(table, new Key("uuid", uuid.toString()))) {
            log.debug("UUID " + uuid + " already in use! Generating a new one..");
            uuid = UUID.randomUUID();
        }

        return uuid;
    }

    public UUID getUUIDFromMinecraftName(String name) throws MojangAPIException {
        return this.formatFromInput(this.mojangAPI.getUUIDOfUsername(name));
    }

    private UUID formatFromInput(String uuid) {
        return UUID.fromString(uuid.length() == 32 ? uuid.substring(0, 8) + '-' + uuid.substring(8, 12) + '-' + uuid.substring(12, 16)
                + '-' + uuid.substring(16, 20) + '-' + uuid.substring(20) : uuid);
    }

}
