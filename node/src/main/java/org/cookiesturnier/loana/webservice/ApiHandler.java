package org.cookiesturnier.loana.webservice;

import org.cookiesturnier.loana.tournament.api.TournamentAPI;
import org.cookiesturnier.loana.tournament.api.exceptions.AlreadyRegisteredException;
import org.cookiesturnier.loana.tournament.api.exceptions.MojangAPIException;
import org.cookiesturnier.loana.tournament.api.exceptions.UnknownPlayerException;
import org.cookiesturnier.loana.tournament.objects.Player;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
public class ApiHandler {

    @PostMapping("/register")
    public RedirectView root(@RequestParam(value = "teamName", required = true) String teamName, @RequestParam(value = "player1", required = true) String player1, @RequestParam(value = "player2", required = true) String player2) {
        TournamentAPI tournamentAPI = new TournamentAPI();

        try {
            Player playerObj1 = tournamentAPI.registerPlayer(player1, "abc");
            Player playerObj2 = tournamentAPI.registerPlayer(player2, "abc");
            tournamentAPI.getRegisteredPlayers().addAll(List.of(playerObj1, playerObj2));
            playerObj1.saveToDatabase();
            playerObj2.saveToDatabase();
            tournamentAPI.registerTeam(teamName, List.of(player1, player2));
            return new RedirectView("/?status=0");
        } catch (MojangAPIException e) {
            return new RedirectView("/?status=1");
        } catch (AlreadyRegisteredException e) {
            return new RedirectView("/?status=2");
        } catch (UnknownPlayerException | IllegalArgumentException e) {
            return new RedirectView("/?status=3");
        }
    }

}
