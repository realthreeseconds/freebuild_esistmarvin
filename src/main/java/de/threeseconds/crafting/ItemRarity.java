package de.threeseconds.crafting;

public enum ItemRarity {

    COMMON("<green>"),
    UNCOMMON("<yellow>"),
    RARE("<blue>"),
    EPIC("<purple>"),
    LEGENDARY("<gold>"),
    STELLAR("<dark_aqua>"),
    DIVINE("<dark_red>");

    private String colorCode;

    ItemRarity(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }
}
