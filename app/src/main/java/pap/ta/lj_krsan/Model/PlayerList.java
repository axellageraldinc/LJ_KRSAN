package pap.ta.lj_krsan.Model;

/**
 * Created by axellageraldinc on 17/11/17.
 */

public class PlayerList {
    private String[] playerName;

    public PlayerList() {
    }

    public PlayerList(String[] playerName) {
        this.playerName = playerName;
    }

    public String[] getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String[] playerName) {
        this.playerName = playerName;
    }
}
