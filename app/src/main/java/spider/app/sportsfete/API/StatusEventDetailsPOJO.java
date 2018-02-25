package spider.app.sportsfete.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by AVINASH on 2/24/2018.
 */
@DatabaseTable(tableName = "StatusEventsDetailTable")
public class StatusEventDetailsPOJO {
    @DatabaseField(generatedId = true)
    long localId;
    @DatabaseField
    @SerializedName("day")
    @Expose
    private Integer day;
    @DatabaseField
    @SerializedName("dept1")
    @Expose
    private String dept1;
    @DatabaseField
    @SerializedName("dept1_score")
    @Expose
    private String dept1Score;
    @DatabaseField
    @SerializedName("dept2")
    @Expose
    private String dept2;
    @DatabaseField
    @SerializedName("dept2_score")
    @Expose
    private String dept2Score;
    @DatabaseField
    @SerializedName("elimination_type")
    @Expose
    private String eliminationType;
    @DatabaseField
    @SerializedName("fixture")
    @Expose
    private String fixture;
    @DatabaseField
    @SerializedName("loser_next_match")
    @Expose
    private String loserNextMatch;
    @DatabaseField
    @SerializedName("name")
    @Expose
    private String name;
    @DatabaseField
    @SerializedName("round")
    @Expose
    private String round;
    @DatabaseField
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @DatabaseField
    @SerializedName("status")
    @Expose
    private String status;
    @DatabaseField
    @SerializedName("venue")
    @Expose
    private String venue;
    @DatabaseField
    @SerializedName("winner")
    @Expose
    private String winner;
    @DatabaseField
    @SerializedName("winner_next_match")
    @Expose
    private String winnerNextMatch;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getDept1() {
        return dept1;
    }

    public void setDept1(String dept1) {
        this.dept1 = dept1;
    }

    public String getDept1Score() {
        return dept1Score;
    }

    public void setDept1Score(String dept1Score) {
        this.dept1Score = dept1Score;
    }

    public String getDept2() {
        return dept2;
    }

    public void setDept2(String dept2) {
        this.dept2 = dept2;
    }

    public String getDept2Score() {
        return dept2Score;
    }

    public void setDept2Score(String dept2Score) {
        this.dept2Score = dept2Score;
    }

    public String getEliminationType() {
        return eliminationType;
    }

    public void setEliminationType(String eleminationType) {
        this.eliminationType = eleminationType;
    }

    public String getFixture() {
        return fixture;
    }

    public void setFixture(String fixture) {
        this.fixture = fixture;
    }

    public String getLoserNextMatch() {
        return loserNextMatch;
    }

    public void setLoserNextMatch(String loserNextMatch) {
        this.loserNextMatch = loserNextMatch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinnerNextMatch() {
        return winnerNextMatch;
    }

    public void setWinnerNextMatch(String winnerNextMatch) {
        this.winnerNextMatch = winnerNextMatch;
    }
}
