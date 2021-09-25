package org.cookiesturnier.loana.tournament.database.dumbstuff.objects;

import lombok.Getter;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 02.12.2020
 * Time: 10:34
 * Project: Pets
 */

@Getter
public class Insert {

    private final String row;
    private final String value;

    public Insert(String row, String value) {
        this.row = row;
        this.value = value;
    }
}
