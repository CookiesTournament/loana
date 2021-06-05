package org.cookiesturnier.loana.webservice;

import org.cookiesturnier.loana.tournament.TournamentManager;
import org.cookiesturnier.loana.tournament.api.TournamentAPI;
import org.cookiesturnier.loana.tournament.api.exceptions.AlreadyRegisteredException;
import org.cookiesturnier.loana.tournament.api.exceptions.MojangAPIException;
import org.cookiesturnier.loana.tournament.api.exceptions.UnknownPlayerException;
import org.cookiesturnier.loana.tournament.objects.Player;
import org.cookiesturnier.loana.tournament.objects.Team;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiHandler {

    @PostMapping("/register")
    public String root(@RequestParam(value = "teamName", required = true) String teamName, @RequestParam(value = "player1", required = true) String player1, @RequestParam(value = "player2", required = true) String player2) {
        TournamentAPI tournamentAPI = new TournamentAPI();
        List<Player> players = tournamentAPI.getRegisteredPlayers();

        try {
            tournamentAPI.registerPlayer(player1, "abc");
            tournamentAPI.registerPlayer(player2, "abc");
            tournamentAPI.registerTeam(teamName, List.of(player1, player2));
            return "Dein Team wurde erfolgreich registriert!";
        } catch (MojangAPIException e) {
            return "Es ist ein Fehler mit der Mojang API aufgetreten! Versuche es später noch einmal!";
        } catch (AlreadyRegisteredException e) {
            return "Mindestens einer der Teilnehmer ist bereits registriert!";
        } catch (UnknownPlayerException e) {
            return "Mindestens einer der Spielernamen ist ungültig!";
        }
    }

}
