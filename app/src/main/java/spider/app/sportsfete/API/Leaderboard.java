package spider.app.sportsfete.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by AVINASH on 3/1/2018.
 */

public class Leaderboard {

    @DatabaseField(generatedId = true)
    long localId;
    @DatabaseField
    @SerializedName("dept")
    @Expose
    private String dept;
    @DatabaseField
    @SerializedName("total")
    @Expose
    private Integer total;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    @SerializedName("splitup")
    @Expose
    private ArrayList<String> splitup;

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<String> getSplitup() {
        return splitup;
    }

    public void setSplitup(ArrayList<String> splitup) {
        this.splitup = splitup;
    }

}
