package application.entities;

import java.util.Objects;

public class BikeRental {

    private Integer bikeId;
    private Integer userId;
    private String startTime;
    private String endTime;

    /**
     * Constructor of bikeRental.
     *
     * @param bikeId    - id of bike
     * @param userId    - id of user
     * @param startTime -  start of rental
     * @param endTime   - end of rental
     */
    public BikeRental(Integer bikeId, Integer userId, String startTime, String endTime) {
        this.bikeId = bikeId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getBikeId() {
        return bikeId;
    }

    public void setBikeId(Integer bikeId) {
        this.bikeId = bikeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BikeRental that = (BikeRental) o;
        return Objects.equals(bikeId, that.bikeId)
            && Objects.equals(userId, that.userId)
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bikeId, userId, startTime, endTime);
    }
}
