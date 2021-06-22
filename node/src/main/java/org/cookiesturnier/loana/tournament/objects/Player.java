package org.cookiesturnier.loana.tournament.objects;

import lombok.Getter;
import lombok.Setter;
import org.cookiesturnier.loana.tournament.TournamentManager;
import org.cookiesturnier.loana.tournament.database.dumbstuff.objects.Insert;
import org.cookiesturnier.loana.tournament.database.dumbstuff.objects.Key;

import javax.persistence.*;
import java.util.UUID;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 01.06.2021
 * Time: 18:52
 * Project: loana
 */

@Getter
@Setter
@Entity
@Table(name = "players")
public class Player {

    @Id
    private UUID uuid;
    private String customName;
    private String discordId;

    public Player(UUID uuid, String customName, String discordId) {
        this.uuid = uuid;
        this.customName = customName;
        this.discordId = discordId;
    }

    public Player() {

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
}
