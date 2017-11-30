package spider.app.sportsfete.API;


/**
 * Created by srikanth on 21/1/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "StandingsTable")
public class Standing {

    @DatabaseField(generatedId = true)
    long localId;
    @DatabaseField
    @SerializedName("id")
    @Expose
    private Integer id;
    @DatabaseField
    @SerializedName("department_name")
    @Expose
    private String departmentName;
    @DatabaseField
    @SerializedName("score")
    @Expose
    private String score;
    @DatabaseField
    private int rank;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public long getLocalId() {
        return localId;
    }

    public void setLocalId(long localId) {
        this.localId = localId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int position) {
        this.rank = position;
    }
}

