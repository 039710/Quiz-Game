package de.hda.fbi.db2.stud.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.*;

/**
 *
 * @author Sascha Bauer, Ahmad Mustain Billah
 *
 */

@Entity
@Table(name = "Player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Player_ID")
    private int playerID;
    @Column(name = "Name")
    private String playerName;
    @OneToMany(targetEntity = Gameinformation.class, mappedBy = "players")
    public List<Gameinformation> gameInfo = new ArrayList<>();

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public void setGameInfo (ArrayList<Gameinformation> tmp){
        this.gameInfo = tmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Player player = (Player) o;
        return playerID == player.playerID &&
                Objects.equals(playerName, player.playerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, playerName);
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerID=" + playerID +
                ", playerName='" + playerName + '\'' +
                '}';
    }



}
