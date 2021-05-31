package org.cookiesturnier.loana.tournament.config;

import lombok.Getter;
import lombok.Setter;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 31.05.2021
 * Time: 20:39
 * Project: loana
 */

@Getter @Setter
public class Config {

    private String databaseHost;
    private String databaseUser;
    private String databasePassword;
    private int databasePort;
    private String databaseName;
    private String streamKey;

}
