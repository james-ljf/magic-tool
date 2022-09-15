package com.magictool.web.util.location;

import lombok.Data;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * 经纬度信息
 *
 * @author kxw
 */
@Data
public class LngLatDTO implements Serializable {

    private static final long serialVersionUID = 4587090435868298210L;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    public LngLatDTO() {
    }

    public LngLatDTO(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LngLatDTO.class.getSimpleName() + "[", "]")
                .add("longitude=" + longitude)
                .add("latitude=" + latitude)
                .toString();
    }
}
