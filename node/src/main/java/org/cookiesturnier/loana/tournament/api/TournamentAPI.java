package org.cookiesturnier.loana.tournament.api;

import com.google.common.collect.Lists;
import org.apache.log4j.Level;
import org.cookiesturnier.loana.tournament.TournamentManager;
import org.cookiesturnier.loana.tournament.objects.Player;
import org.cookiesturnier.loana.tournament.objects.Team;
import org.cookiesturnier.loana.tournament.utils.TeamManager;

import java.util.List;
import java.util.UUID;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 01.06.2021
 * Time: 18:51
 * Project: loana
 */

public class TournamentAPI {

    private final TeamManager teamManager;

    public TournamentAPI() {
        this.teamManager = TournamentManager.getInstance().getTeamManager();
    }

    /**
     * Gets a list containing all the registered teams
     * @return List containing all the registered teams
     */
    public List<Team> getRegisteredTeams() {
        return teamManager.getRegisteredTeams();
    }

    /**
     * Gets a list containing all the registered players
     * @return List containing all the registered players
     */
    public List<Player> getRegisteredPlayers() {
        return teamManager.getLoadedPlayers();
    }

    /**
     * Registers a player as a tournament member
     * @param ingameName Minecraft name of the player
     * @param discordTag Discord tag of the player
     * @return If the registration has been successful
     */
    public boolean registerPlayer(String ingameName, String discordTag) {
        final UUID uuid = teamManager.getUUIDFromMinecraftName(ingameName);

        if(uuid == null) return false;
        if(teamManager.getLoadedPlayers().stream().anyMatch(player -> player.getUuid().equals(uuid))) return false;

        try {
            teamManager.registerPlayer(uuid, ingameName, discordTag);
            return true;
        } catch (Exception exception) {
            TournamentManager.getInstance().getLogger().log(Level.ERROR, "An error occurred while registering the player.", exception);
            return false;
        }
    }

    /**
     * Registers a team
     * @param teamName Name of the team
     * @param memberNames List containing the members' Minecraft names
     * @return If the registration has been successful
     */
    public boolean registerTeam(String teamName, List<String> memberNames) {
        final List<Player> members = Lists.newCopyOnWriteArrayList();

        memberNames.forEach(memberName -> {
            final Player player = this.teamManager.getLoadedPlayers().stream()
                    .filter(player1 -> player1.getCustomName().equals(memberName))
                    .findAny().orElse(null);

            if(player == null) {
                TournamentManager.getInstance().getLogger().log(Level.ERROR, "Couldn't find a player with the name " + memberName + "!");
                return;
            }

            members.add(player);
        });

        if(members.isEmpty()) return false;

        try {
            teamManager.registerTeam(teamName, members);
            return true;
        } catch (Exception exception) {
            TournamentManager.getInstance().getLogger().log(Level.ERROR, "An error occurred while registering the team.", exception);
            return false;
        }
    }

}
