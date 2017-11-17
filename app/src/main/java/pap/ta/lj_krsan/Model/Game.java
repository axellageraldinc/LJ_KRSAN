package pap.ta.lj_krsan.Model;

/**
 * Created by axellageraldinc on 17/11/17.
 */

public class Game {
    private String id, objective;
    private int player_amount, progress, status;
    private String player_list;

    public Game() {
    }

    public Game(String id, String objective, int player_amount, int progress, String player_list, int status) {
        this.id = id;
        this.objective = objective;
        this.player_amount = player_amount;
        this.progress = progress;
        this.player_list = player_list;
        this.status = status;
    }

    public Game(String objective, int player_amount, int progress, String player_list) {
        this.objective = objective;
        this.player_amount = player_amount;
        this.progress = progress;
        this.player_list = player_list;
    }

    public Game(int player_amount, int progress, String player_list) {
        this.player_amount = player_amount;
        this.progress = progress;
        this.player_list = player_list;
    }

    public Game(int progress, String player_list) {
        this.progress = progress;
        this.player_list = player_list;
    }

    public Game(String player_list) {
        this.player_list = player_list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public int getPlayer_amount() {
        return player_amount;
    }

    public void setPlayer_amount(int player_amount) {
        this.player_amount = player_amount;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getPlayer_list() {
        return player_list;
    }

    public void setPlayer_list(String player_list) {
        this.player_list = player_list;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
