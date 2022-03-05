package org.cookiesturnier.loana.tournament.objects;

import lombok.Getter;
import lombok.Setter;
import org.cookiesturnier.loana.tournament.TournamentManager;
import org.cookiesturnier.loana.tournament.database.objects.Insert;
import org.cookiesturnier.loana.tournament.database.objects.Key;
import org.cookiesturnier.loana.tournament.enums.IngameTeamColor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 01.06.2021
 * Time: 18:52
 * Project: loana
 */

@Getter @Setter
public class Team {

    private final UUID uuid;
    private final String teamName;
    private final List<Player> members;
    private IngameTeamColor ingameTeamColor;

    public Team(UUID uuid, String teamName, List<Player> members) {
        this.uuid = uuid;
        this.teamName = teamName;
        this.members = members;
    }

    public void addPlayer(Player player) {
        if(!this.members.contains(player))
            this.members.add(player);
    }

    public void removePlayer(Player player) {
        this.members.remove(player);
    }

    //TODO rework
    public void saveToDatabase() throws IOException, SQLException {
        final Insert[] inserts = new Insert[3];
        final String[] memberUUIDArray = new String[2];

        final AtomicInteger integer = new AtomicInteger(-1);
        this.members.forEach(player -> memberUUIDArray[integer.incrementAndGet()] = player.getUuid().toString());

        inserts[0] = new Insert("uuid", this.uuid.toString());
        inserts[1] = new Insert("name", this.teamName);
        inserts[2] = new Insert("members", Arrays.toString(memberUUIDArray));

        if(TournamentManager.getInstance().getDatabaseAdapter().containsEntry("teams", new Key("uuid", this.uuid.toString())))
            TournamentManager.getInstance().getDatabaseAdapter().updateValues("teams", new Key("uuid", this.uuid.toString()), inserts);
        else
            TournamentManager.getInstance().getDatabaseAdapter().insertIntoTable("teams", inserts);
    }
}
