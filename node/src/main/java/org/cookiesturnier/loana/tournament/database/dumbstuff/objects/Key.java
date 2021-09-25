package org.cookiesturnier.loana.tournament.database.dumbstuff.objects;

import lombok.Getter;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 02.12.2020
 * Time: 10:34
 * Project: Pets
 */

@Getter
public class Key {

    private final String row;
    private final String keyWord;

    public Key(String row, String keyWord) {
        this.row = row;
        this.keyWord = keyWord;
    }
}
