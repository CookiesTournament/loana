package org.cookiesturnier.loana.tournament.api;

import com.google.common.collect.Lists;
import org.apache.log4j.Level;
import org.cookiesturnier.loana.tournament.TournamentManager;
import org.cookiesturnier.loana.tournament.api.exceptions.AlreadyRegisteredException;
import org.cookiesturnier.loana.tournament.api.exceptions.MojangAPIException;
import org.cookiesturnier.loana.tournament.api.exceptions.UnknownPlayerException;
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
    public Player registerPlayer(String ingameName, String discordTag) throws MojangAPIException, AlreadyRegisteredException, UnknownPlayerException, IllegalArgumentException {
        if(!this.isValidUsername(ingameName))
            throw new IllegalArgumentException("Invalid username!");

        final UUID uuid = teamManager.getUUIDFromMinecraftName(ingameName);

        if(uuid == null)
            throw new UnknownPlayerException("The player with the name '" + ingameName + "' couldn't be found!");

        if(teamManager.getLoadedPlayers().stream().anyMatch(player -> player.getUuid().equals(uuid)))
            throw new AlreadyRegisteredException("This player is already registered!");

        try {
            return teamManager.registerPlayer(uuid, ingameName, discordTag);
        } catch (Exception exception) {
            TournamentManager.getInstance().getLogger().log(Level.ERROR, "An error occurred while registering the player", exception);
            return null;
        }
    }

    /**
     * Registers a team
     * @param teamName Name of the team
     * @param memberNames List containing the members' Minecraft names
     * @return If the registration has been successful
     */
    public Team registerTeam(String teamName, List<String> memberNames) throws UnknownPlayerException {
        final List<Player> members = Lists.newCopyOnWriteArrayList();

        for(String memberName : memberNames) {
            final Player player = this.teamManager.getLoadedPlayers().stream()
                    .filter(player1 -> player1.getCustomName().equals(memberName))
                    .findAny().orElse(null);

            if(player == null)
                throw new UnknownPlayerException("Couldn't find a player with the name " + memberName + "!");

            members.add(player);
        }

        if(members.isEmpty())
            throw new NullPointerException("No members found?");

        try {
            return teamManager.registerTeam(teamName, members);
        } catch (Exception exception) {
            TournamentManager.getInstance().getLogger().log(Level.ERROR, "An error occurred while registering the team.", exception);
            return null;
        }
    }

    /**
     * Checks if the username is a valid Minecraft username
     * @param username Username that should be checked
     * @return If the username is a valid Minecraft username
     */
    public boolean isValidUsername(String username) {
        return username.matches("^[\\w]{3,16}$");
    }

}
