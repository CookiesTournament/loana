package org.cookiesturnier.loana;

import org.cookiesturnier.loana.tournament.TournamentManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NodeApplication {

    public static void main(String[] args) {
        new TournamentManager();
        SpringApplication.run(NodeApplication.class, args);
    }

}
