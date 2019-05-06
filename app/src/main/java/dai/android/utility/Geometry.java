package dai.android.utility;

// 几何学类
public class Geometry {

    // 点
    public static class Point {
        public final float X, Y, Z;

        public Point(float x, float y, float z) {
            X = x;
            Y = y;
            Z = z;
        }

        public Point translateY(float distance) {
            return new Point(X, Y + distance, Z);
        }
    }


    // 圆
    public static class Circle {
        public final Point Center;
        public final float Radius;

        public Circle(Point center, float radius) {
            Center = center;
            Radius = radius;
        }

        public Circle scale(float scale) {
            return new Circle(Center, Radius * scale);
        }
    }


    // 圆柱
    public static class Cylinder {
        public final Point Center;
        public final float Radius;
        public final float Height;

        public Cylinder(Point center, float radius, float height) {
            Center = center;
            Radius = radius;
            Height = height;
        }
    }

}
