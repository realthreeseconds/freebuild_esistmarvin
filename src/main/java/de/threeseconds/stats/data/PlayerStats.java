package de.threeseconds.stats.data;

import de.threeseconds.util.FreeBuildPlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class PlayerStats {

    private Player player;
    private Double health;
    private Integer souls;
    private Integer defense;
    private Integer strength;
    private Integer speed;
    private Double critChance;
    private Double critDamage;


    public PlayerStats(Player player, Consumer<PlayerStats> playerStatsConsumer) {
        this.player = player;
        playerStatsConsumer.accept(this);
    }

    public Player player() {
        return player;
    }

    public Double health() {
        return health;
    }

    public void health(Double health) {
        this.health = health;
    }

    public Integer souls() {
        return souls;
    }

    public void souls(Integer souls) {
        this.souls = souls;
    }

    public Integer defense() {
        return defense;
    }

    public void defense(Integer defense) {
        this.defense = defense;
    }

    public Integer strength() {
        return strength;
    }

    public void strength(Integer strength) {
        this.strength = strength;
    }

    public Integer speed() {
        return speed;
    }

    public void speed(Integer speed) {
        this.speed = speed;
        //this.player().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
    }

    public Double critChance() {
        return critChance;
    }

    public void critChance(Double critChance) {
        this.critChance = critChance;
    }

    public Double critDamage() {
        return critDamage;
    }

    public void critDamage(Double critDamage) {
        this.critDamage = critDamage;
    }
}
