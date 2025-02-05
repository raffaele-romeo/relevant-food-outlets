package solution.model;

import com.google.gson.annotations.SerializedName;

public class FoodOutletsData {
    private long id;
    private String name;
    private String city;

    @SerializedName("estimated_cost")
    private int estimatedCost;

    @SerializedName("user_rating")
    private UserRating userRating;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(int estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public UserRating getUserRating() {
        return userRating;
    }

    public void setUserRating(UserRating userRating) {
        this.userRating = userRating;
    }
}