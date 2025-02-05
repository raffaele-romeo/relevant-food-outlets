package solution.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodOutletsResponse {
    private int page;
    @SerializedName("total_pages")
    private int totalPages;

    private List<FoodOutletsData> data;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<FoodOutletsData> getData() {
        return data;
    }

    public void setData(List<FoodOutletsData> data) {
        this.data = data;
    }
}

