package as.r_petals.dto.occasions;

public class OccasionStatsResponse {

    private long totalOccasions;
    private long activeOccasions;
    private long upcomingOccasions;

    public OccasionStatsResponse() {
    }

    public OccasionStatsResponse(
            long totalOccasions,
            long activeOccasions,
            long upcomingOccasions
    ) {
        this.totalOccasions = totalOccasions;
        this.activeOccasions = activeOccasions;
        this.upcomingOccasions = upcomingOccasions;
    }

    public long getTotalOccasions() {
        return totalOccasions;
    }

    public void setTotalOccasions(long totalOccasions) {
        this.totalOccasions = totalOccasions;
    }

    public long getActiveOccasions() {
        return activeOccasions;
    }

    public void setActiveOccasions(long activeOccasions) {
        this.activeOccasions = activeOccasions;
    }

    public long getUpcomingOccasions() {
        return upcomingOccasions;
    }

    public void setUpcomingOccasions(long upcomingOccasions) {
        this.upcomingOccasions = upcomingOccasions;
    }
}