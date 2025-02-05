package solution.model;

import com.google.gson.annotations.SerializedName;

public class UserRating {
    @SerializedName("average_rating")
    private double averageRating;
    private int votes;

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}