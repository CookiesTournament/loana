package org.cookiesturnier.loana.tournament.database.objects;

import lombok.Getter;
import org.cookiesturnier.loana.tournament.database.enums.RowType;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 02.12.2020
 * Time: 10:34
 * Project: Pets
 */

@Getter
public class Row {

    private final String name;
    private final RowType type;

    public Row(String name, RowType type) {
        this.name = name;
        this.type = type;
    }
}
