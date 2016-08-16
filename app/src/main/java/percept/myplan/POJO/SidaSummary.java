package percept.myplan.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by percept on 16/8/16.
 */

public class SidaSummary {
    @SerializedName("week_number")
    private String Week_Number;
    @SerializedName("avg_score")
    private String Avg_Score;

    public SidaSummary(String week_Number, String avg_Score) {
        Week_Number = week_Number;
        Avg_Score = avg_Score;
    }

    public String getWeek_Number() {
        return Week_Number;
    }

    public void setWeek_Number(String week_Number) {
        Week_Number = week_Number;
    }

    public String getAvg_Score() {
        return Avg_Score;
    }

    public void setAvg_Score(String avg_Score) {
        Avg_Score = avg_Score;
    }
}
