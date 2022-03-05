package org.cookiesturnier.loana.tournament.objects;

import lombok.Getter;
import org.cookiesturnier.loana.tournament.TournamentManager;
import org.cookiesturnier.loana.tournament.database.objects.Insert;
import org.cookiesturnier.loana.tournament.database.objects.Key;

import java.util.UUID;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 01.06.2021
 * Time: 18:52
 * Project: loana
 */

@Getter
public class Player {

    private final UUID uuid;
    private final String customName;
    private final String discordId;

    public Player(UUID uuid, String customName, String discordId) {
        this.uuid = uuid;
        this.customName = customName;
        this.discordId = discordId;
    }

    //TODO rework
    public void saveToDatabase() {
        final Insert[] inserts = new Insert[3];

        inserts[0] = new Insert("uuid", this.uuid.toString());
        inserts[1] = new Insert("customName", this.customName);
        inserts[2] = new Insert("discordId", this.discordId);

        if(TournamentManager.getInstance().getDatabaseAdapter().containsEntry("players", new Key("uuid", this.uuid.toString())))
            TournamentManager.getInstance().getDatabaseAdapter().updateValues("players", new Key("uuid", this.uuid.toString()), inserts);
        else
            TournamentManager.getInstance().getDatabaseAdapter().insertIntoTable("players", inserts);
    }

    @Override
    public String toString() {
        return "Player{" +
                "uuid=" + uuid +
                ", customName='" + customName + '\'' +
                ", discordId='" + discordId + '\'' +
                '}';
    }
}
