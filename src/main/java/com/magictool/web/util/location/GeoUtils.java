package com.magictool.web.util.location;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * 经纬度工具
 *
 * @author ljf
 * @date 2022/9/15 16:26
 */
@Slf4j
public class GeoUtils {

    /**
     * 地球半径,单位 km
     */
    private static final double EARTH_RADIUS = 6378.137;

    /**
     * 求B点经纬度
     *
     * @param knowA    已知点的经纬度，
     * @param distance AB两地的距离  单位km
     * @param angle    AB连线与正北方向的夹角（0~360）
     * @return B点的经纬度
     */
    public static MyLatLng getMyLatLng(MyLatLng knowA, double distance, double angle) {

        double dx = distance * 1000 * Math.sin(Math.toRadians(angle));
        double dy = distance * 1000 * Math.cos(Math.toRadians(angle));

        double bjd = (dx / knowA.ed + knowA.mRadLo) * 180. / Math.PI;
        double bwd = (dy / knowA.ec + knowA.mRadLa) * 180. / Math.PI;
        return new MyLatLng(bjd, bwd);
    }


    /**
     * 获取AB连线与正北方向的角度
     *
     * @param a A点的经纬度
     * @param b B点的经纬度
     * @return AB连线与正北方向的角度（0~360）
     */
    public static double getAngle(MyLatLng a, MyLatLng b) {
        double dx = (b.mRadLo - a.mRadLo) * a.ed;
        double dy = (b.mRadLa - a.mRadLa) * a.ec;
        double angle;
        angle = Math.atan(Math.abs(dx / dy)) * 180. / Math.PI;
        double dLo = b.mLongitude - a.mLongitude;
        double dLa = b.mLatitude - a.mLatitude;
        if (dLo > 0 && dLa <= 0) {
            angle = (90. - angle) + 90;
        } else if (dLo <= 0 && dLa < 0) {
            angle = angle + 180.;
        } else if (dLo < 0 && dLa >= 0) {
            angle = (90. - angle) + 270;
        }
        return angle;
    }

    static class MyLatLng {
        static final double RC = 6378137;
        static final double RJ = 6356725;
        double mLoDeg;
        double mLoMin;
        double mLoSec;
        double mLaDeg;
        double mLaMin;
        double mLaSec;
        double mLongitude;
        double mLatitude;
        double mRadLo;
        double mRadLa;
        double ec;
        double ed;

        public MyLatLng(double longitude, double latitude) {
            mLoDeg = (int) longitude;
            mLoMin = (int) ((longitude - mLoDeg) * 60);
            mLoSec = (longitude - mLoDeg - mLoMin / 60.) * 3600;

            mLaDeg = (int) latitude;
            mLaMin = (int) ((latitude - mLaDeg) * 60);
            mLaSec = (latitude - mLaDeg - mLaMin / 60.) * 3600;

            mLongitude = longitude;
            mLatitude = latitude;
            mRadLo = longitude * Math.PI / 180.;
            mRadLa = latitude * Math.PI / 180.;
            ec = RJ + (RC - RJ) * (90. - mLatitude) / 90.;
            ed = ec * Math.cos(mRadLa);
        }
    }


    public static Box getBox(Point p1, Point p2, double distance) {
        MyLatLng a = new MyLatLng(p1.getX(), p1.getY());
        MyLatLng b = new MyLatLng(p2.getX(), p2.getY());
        //求出AB与正北方向的角度
        double angle = getAngle(a, b);
        double d1 = 180D - angle;
        double d2 = 90D - d1;
        //B点的右上角
        LngLatDTO bx = calLocationByDistanceAndLocationAndDirection(d2, p2.getX(), p2.getY(), distance);
        Point bp = new Point(bx.getLatitude(), bx.getLongitude());
        //A点的左下角
        LngLatDTO ax = calLocationByDistanceAndLocationAndDirection(d2 + 180D, p1.getX(), p1.getY(), distance);
        Point ap = new Point(ax.getLatitude(), ax.getLongitude());
        return new Box(ap, bp);

    }

    /**
     * 根据经纬度，计算两点间的距离
     *
     * @param longitude1 第一个点的经度
     * @param latitude1  第一个点的纬度
     * @param longitude2 第二个点的经度
     * @param latitude2  第二个点的纬度
     * @return 返回距离 单位千米
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 纬度
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // 经度
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);

        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lng1 - lng2;

        // 计算两点距离的公式
        double s = 2 * Math.asin(Math
                .sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘地球半径, 返回单位: 千米
        s = s * EARTH_RADIUS;
        return s;
    }

    /**
     * 根据经纬度，计算点是否在圈内
     *
     * @param longitude1 圆心的经度
     * @param latitude1  圆心的纬度
     * @param longitude2 位置的经度
     * @param latitude2  位置的纬度
     * @param radius     圈的半径
     * @return 返回boolean
     */
    public static boolean isPointInCircle(double longitude1, double latitude1, double longitude2, double latitude2, double radius) {
        double s = getDistance(longitude1, latitude1, longitude2, latitude2);
        return (s * 1000 <= radius);
    }

    /**
     * 根据经纬度，点到线段的最短距离(m米)
     *
     * @param longitude0 位置的经度
     * @param latitude0  位置的纬度
     * @param longitude1 第一个线段端点经度
     * @param latitude1  第一个线段端点纬度
     * @param longitude2 第二个线段端点经度
     * @param latitude2  第二个线段端点纬度
     * @return 返回double (m米)
     */
    public static double distancePointToline(double longitude0, double latitude0, double longitude1, double latitude1, double longitude2, double latitude2) {
        double space = 0;
        double a;
        double b;
        double c;
        // 线段的长度
        a = getDistance(longitude1, latitude1, longitude2, latitude2);
        // (x1,y1)到点的距离
        b = getDistance(longitude1, latitude1, longitude0, latitude0);
        // (x2,y2)到点的距离
        c = getDistance(longitude2, latitude2, longitude0, latitude0);
        if (c * c >= a * a + b * b) {
            // 组成直角三角形或钝角三角形，(x1,y1)为直角或钝角
            space = b;
        } else if (b * b >= a * a + c * c) {
            // 组成直角三角形或钝角三角形，(x2,y2)为直角或钝角
            space = c;
        } else {
            // 组成锐角三角形，则求三角形的高
            // 半周长
            double p = (a + b + c) / 2;
            // 海伦公式求面积
            double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));
            // 返回点到线的距离（利用三角形面积公式求高）
            space = 2 * s / a;
        }
        return space * 1000;
    }

    /**
     * 判断点是否在多边形内部
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param points    经纬度集合
     * @return boolean
     */
    public static boolean isPointInPolygon(double longitude, double latitude, LngLatDTO[] points) {
        int iSum;
        int iCount;
        int iIndex;
        double dLon1;
        double dLon2;
        double dLat1;
        double dLat2;
        double dLon;
        if (points.length < 3) {
            return false;
        }
        iSum = 0;
        iCount = points.length;
        for (iIndex = 0; iIndex < iCount; iIndex++) {
            if (iIndex == iCount - 1) {
                dLon1 = points[iIndex].getLongitude();
                dLat1 = points[iIndex].getLatitude();
                dLon2 = points[0].getLongitude();
                dLat2 = points[0].getLatitude();
            } else {
                dLon1 = points[iIndex].getLongitude();
                dLat1 = points[iIndex].getLatitude();
                dLon2 = points[iIndex + 1].getLongitude();
                dLat2 = points[iIndex + 1].getLatitude();
            }
            // 以下语句判断A点是否在边的两端点的水平平行线之间，在则可能有交点，开始判断交点是否在左射线上
            if ((latitude >= dLat1 && latitude < dLat2) || (latitude >= dLat2 && latitude < dLat1)) {
                if (Math.abs(dLat1 - dLat2) > 0) {
                    //得到 A点向左射线与边的交点的x坐标：
                    dLon = dLon1 - ((dLon1 - dLon2) * (dLat1 - latitude)) / (dLat1 - dLat2);
                    // 如果交点在A点左侧（说明是做射线与 边的交点），则射线与边的全部交点数加一：
                    if (dLon < longitude) {
                        iSum++;
                    }
                }
            }
        }
        return (iSum % 2) != 0;
    }

    /**
     * 两【线段】是否相交
     *
     * @param l1x1 线段1的x1
     * @param l1y1 线段1的y1
     * @param l1x2 线段1的x2
     * @param l1y2 线段1的y2
     * @param l2x1 线段2的x1
     * @param l2y1 线段2的y1
     * @param l2x2 线段2的x2
     * @param l2y2 线段2的y2
     * @return 是否相交
     */
    public static boolean isLineIntersection(double l1x1, double l1y1, double l1x2, double l1y2, double l2x1, double l2y1, double l2x2, double l2y2) {
        // 快速排斥实验 首先判断两条线段在 x 以及 y 坐标的投影是否有重合。 有一个为真，则代表两线段必不可交。
        if (Math.max(l1x1, l1x2) < Math.min(l2x1, l2x2) || Math.max(l1y1, l1y2) < Math.min(l2y1, l2y2) || Math.max(l2x1, l2x2) < Math.min(l1x1, l1x2) || Math.max(l2y1, l2y2) < Math.min(l1y1, l1y2)) {
            return false;
        }
        // 跨立实验  如果相交则矢量叉积异号或为零，大于零则不相交

        return !((((l1x1 - l2x1) * (l2y2 - l2y1) - (l1y1 - l2y1) * (l2x2 - l2x1))
                * ((l1x2 - l2x1) * (l2y2 - l2y1) - (l1y2 - l2y1) * (l2x2 - l2x1))) > 0
                || (((l2x1 - l1x1) * (l1y2 - l1y1) - (l2y1 - l1y1) * (l1x2 - l1x1))
                * ((l2x2 - l1x1) * (l1y2 - l1y1) - (l2y2 - l1y1) * (l1x2 - l1x1))) > 0);
    }

    /**
     * 判断多边形
     * 除邻边外是否存在交叉的情况（复杂多边形，如五角星）
     *
     */
    public static boolean isPolygonExistLineIntersection(List<LngLatDTO> pointList) {
        // 1、如果传入的点不足3点以上，返回false
        if (pointList.size() <= 3) {
            return false;
        }

        // 2、首尾连接
        LngLatDTO lastLngLatDTO = new LngLatDTO(pointList.get(0).getLongitude(), pointList.get(0).getLatitude());
        pointList.add(lastLngLatDTO);

        // 3、不计算邻边，计算邻边外的线短是否相交
        boolean intersection = false;
        for (int i = 0; i < pointList.size(); i++) {
            // （1）不计算顺时针的邻边
            for (int j = i + 2; j < pointList.size() - 1; j++) {
                // （2）不计算逆时针的邻边
                if (!(pointList.get(i).getLongitude().equals(pointList.get(j + 1).getLongitude())
                        && pointList.get(i).getLatitude().equals(pointList.get(j + 1).getLatitude()))) {
                    intersection = isLineIntersection(
                            pointList.get(i).getLongitude(), pointList.get(i).getLatitude(), pointList.get(i + 1).getLongitude(), pointList.get(i + 1).getLatitude(),
                            pointList.get(j).getLongitude(), pointList.get(j).getLatitude(), pointList.get(j + 1).getLongitude(), pointList.get(j + 1).getLatitude());
                }
                if (intersection) {
                    break;
                }
            }
            if (intersection) {
                break;
            }
        }
        return intersection;
    }

    /**
     * 通过已知点的经纬度，相对角度，距离计算另一点的经纬度
     *
     * @param angle      角度从正北方开始到目标点的角度；
     * @param longitude, latitude 起始点位置经纬度，十进制;
     * @param distance   距离（单位千米）;
     * @return 经纬度信息
     */
    public static LngLatDTO calLocationByDistanceAndLocationAndDirection(double angle, double longitude, double latitude, double distance) {
        LngLatDTO result = new LngLatDTO();
        // 将距离转换成经度的计算公式
        double δ = distance / (EARTH_RADIUS * 1000) * 1000;

        // 转换为raDian，否则结果会不正确
        angle = Math.toRadians(angle);
        longitude = Math.toRadians(longitude);
        latitude = Math.toRadians(latitude);
        double lat = Math.asin(Math.sin(latitude) * Math.cos(δ) + Math.cos(latitude) * Math.sin(δ) * Math.cos(angle));
        double lon = longitude + Math.atan2(Math.sin(angle) * Math.sin(δ) * Math.cos(latitude), Math.cos(δ) - Math.sin(latitude) * Math.sin(lat));

        // 转为正常的10进制经纬度
        lon = Math.toDegrees(lon);
        lat = Math.toDegrees(lat);
        result.setLatitude(lat);
        result.setLongitude(lon);
        return result;
    }

    private static void testPolyIntersection() {
        List<LngLatDTO> pointList = new ArrayList<>();
        LngLatDTO p1 = new LngLatDTO(117.043730, 35.061620);
        LngLatDTO p2 = new LngLatDTO(117.315090, 35.072965);
        LngLatDTO p3 = new LngLatDTO(117.459969, 35.265585);
        LngLatDTO p4 = new LngLatDTO(117.572652, 35.095650);
        LngLatDTO p5 = new LngLatDTO(117.839413, 35.086199);
        LngLatDTO p6 = new LngLatDTO(117.600248, 34.929146);
        LngLatDTO p7 = new LngLatDTO(117.729029, 34.735724);
        LngLatDTO p8 = new LngLatDTO(117.450770, 34.881781);
        LngLatDTO p9 = new LngLatDTO(117.305892, 34.745216);
        LngLatDTO p10 = new LngLatDTO(117.223104, 35.156112);

        pointList.add(p1);
        pointList.add(p2);
        pointList.add(p3);
        pointList.add(p4);
        pointList.add(p5);
        pointList.add(p6);
        pointList.add(p7);
        pointList.add(p8);
        pointList.add(p9);
        pointList.add(p10);

        boolean intersection = isPolygonExistLineIntersection(pointList);
        log.info("内部（除邻边外）是否存在交叉 ： " + intersection);
    }

    private static void testLineIntersection() {
        double line1p1x = 120.446502;
        double line1p1y = 36.225281;
        double line1p2x = 120.447670;
        double line1p2y = 36.226220;
        double line2p1x = 120.446565;
        double line2p1y = 36.225496;
        double line2p2x = 120.446628;
        double line2p2y = 36.225267;

        boolean intersection = isLineIntersection(line1p1x, line1p1y, line1p2x, line1p2y, line2p1x, line2p1y, line2p2x, line2p2y);
        log.info("两线段是否相交:" + intersection);
    }

    private static void testPointInPolygon() {
        double longitude = 113.507861;
        double latitude = 23.184144;

        LngLatDTO[] points = new LngLatDTO[]{
                new LngLatDTO(113.501585, 23.184065),
                new LngLatDTO(113.505208, 23.188556),
                new LngLatDTO(113.512884, 23.183849),
                new LngLatDTO(113.510681, 23.180575)};
        boolean in = isPointInPolygon(longitude, latitude, points);
        log.info("点是否在多边形内部 :" + in);
    }

    private static void getShortestDisdancePointToline() {

        double distance = distancePointToline(113.50022860826371, 23.1758018094291, 113.5001110, 23.1756410, 113.5001170, 23.1750410);
        log.info("点到线段最近距离" + distance + "m");
    }
}
