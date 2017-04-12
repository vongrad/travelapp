package dk.itu.vongrad.travelapp.realm.model;

import com.estimote.sdk.Beacon;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Adam Vongrej on 4/12/17.
 */

public class Location extends RealmObject {

    private Integer floor;
    private Integer area;
    private Integer room;
    private Integer number;

    private Date createdAt;

    public Location() {}

    public Location(Integer floor, Integer area, Integer room, Integer number) {
        this.floor = floor;
        this.area = area;
        this.room = room;
        this.number = number;
        this.createdAt = new Date();
    }

    public Location(Beacon beacon) {
        this.floor = beacon.getMajor();

        String minor = String.valueOf(beacon.getMinor());
        this.area = Integer.valueOf(minor.substring(0, 1));
        this.room = Integer.valueOf(minor.substring(1, 3));
        this.number = Integer.valueOf(minor.substring(3, 4));
        this.createdAt = new Date();
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Floor: " + floor + ", room: " + room;
    }

    @Override
    public int hashCode() {
        int result = floor != null ? floor.hashCode() : 0;
        result = 31 * result + (area != null ? area.hashCode() : 0);
        result = 31 * result + (room != null ? room.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }
}
