package spider.app.sportsfete.FireBaseServices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.security.Timestamp;
import java.util.ArrayList;

/**
 * Created by praba1110 on 2/2/17.
 */

public class Score {

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
    private String dept1_score;
    @DatabaseField
    @SerializedName("dept2")
    @Expose
    private String dept2;
    @DatabaseField
    @SerializedName("dept2_score")
    @Expose
    private String dept2_score;
    @DatabaseField
    @SerializedName("elimination_type")
    @Expose
    private String elimination_type;
    @DatabaseField
    @SerializedName("fixture")
    @Expose
    private Integer fixture;
    @DatabaseField
    @SerializedName("fixture_index")
    @Expose
    private Integer fixture_index;
    @DatabaseField
    @SerializedName("fixture_type")
    @Expose
    private Integer fixture_type;
    @DatabaseField
    @SerializedName("group")
    @Expose
    private String group;
    @DatabaseField
    @SerializedName("hint")
    @Expose
    private String hint;
    @DatabaseField
    @SerializedName("loser_dept_next")
    @Expose
    private String loser_dept_next;
    @DatabaseField
    @SerializedName("loser_next_match")
    @Expose
    private String loser_next_match;
    @DatabaseField
    @SerializedName("name")
    @Expose
    private String name;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    @SerializedName("participating_teams")
    @Expose
    private ArrayList<String> participating_teams = null;
    @DatabaseField
    @SerializedName("round")
    @Expose
    private String round;
    @DatabaseField
    @SerializedName("start_time")
    @Expose
    private Long start_time;
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
    @SerializedName("winner_dept_next")
    @Expose
    private Integer winner_dept_next;
    @DatabaseField
    @SerializedName("winner_next_match")
    @Expose
    private Integer winner_next_match;


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

    public String getDept1_score() {
        return dept1_score;
    }

    public void setDept1_score(String dept1Score) {
        this.dept2_score = dept1Score;
    }

    public String getDept2() {
        return dept2;
    }

    public void setDept2(String dept2) {
        this.dept2 = dept2;
    }

    public String getDept2_score() {
        return dept2_score;
    }

    public void setDept2_score(String dept2Score) {
        this.dept2_score = dept2Score;
    }

    public String getElimination_type() {
        return elimination_type;
    }

    public void setElimination_type(String eliminationType) {
        this.elimination_type = eliminationType;
    }

    public Integer getFixture() {
        return fixture;
    }

    public void setFixture(Integer fixture) {
        this.fixture = fixture;
    }

    public Integer getFixture_index() {
        return fixture_index;
    }

    public void setFixture_index(Integer fixtureIndex) {
        this.fixture_index = fixtureIndex;
    }

    public Integer getFixture_type() {
        return fixture_type;
    }

    public void setFixture_type(Integer fixtureType) {
        this.fixture_type = fixtureType;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getLoser_dept_next() {
        return loser_dept_next;
    }

    public void setLoser_dept_next(String loserDeptNext) {
        this.loser_dept_next = loserDeptNext;
    }

    public String getLoser_next_match() {
        return loser_next_match;
    }

    public void setLoser_next_match(String loserNextMatch) {
        this.loser_next_match = loserNextMatch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getParticipating_teams() {
        return participating_teams;
    }

    public void setParticipating_teams(ArrayList<String> participatingTeams) {
        this.participating_teams = participatingTeams;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long startTime) {
        this.start_time = startTime;
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

    public Integer getWinner_dept_next() {
        return winner_dept_next;
    }

    public void setWinner_dept_next(Integer winnerDeptNext) {
        this.winner_dept_next = winnerDeptNext;
    }

    public Integer getWinner_next_match() {
        return winner_next_match;
    }

    public void setWinner_next_match(Integer winnerNextMatch) {
        this.winner_next_match = winnerNextMatch;
    }
}
