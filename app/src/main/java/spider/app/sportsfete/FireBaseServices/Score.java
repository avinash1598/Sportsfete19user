package spider.app.sportsfete.FireBaseServices;

import java.security.Timestamp;

/**
 * Created by praba1110 on 2/2/17.
 */

public class Score {
    private String score1;
    private String score2;
    private String hint;
    private String timestamp;


    public void setScore1(String score1) {
        this.score1 = score1;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public String getHint() {
        return hint;
    }

    public String getScore1() {
        return score1;
    }

    public String getScore2() {
        return score2;
    }
}
