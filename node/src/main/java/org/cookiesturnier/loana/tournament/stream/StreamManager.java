package org.cookiesturnier.loana.tournament.stream;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import net.twasi.obsremotejava.OBSRemoteController;
import org.apache.log4j.Level;

@Slf4j
public class StreamManager {

    public StreamManager(String obsPassword) {
        OBSRemoteController obs = new OBSRemoteController("ws://localhost:4444", false, obsPassword);
        obs.registerConnectCallback(getVersionResponse -> {

        });
        obs.getCurrentProfile(resp -> {
            log.debug("ProfileName: " + resp.getProfileName());
        });
    }
}