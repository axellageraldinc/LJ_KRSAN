package pap.ta.lj_krsan.Model;

/**
 * Created by axellageraldinc on 22/11/17.
 */

public class Score {
    int id, score;
    String id_user, username, waktuSelesai;

    public Score() {
    }

    public Score(int score, String id_user, String waktuSelesai) {
        this.score = score;
        this.id_user = id_user;
        this.waktuSelesai = waktuSelesai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(String waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }
}
