package dai.android.utility;

public class MatrixHelper {

    private MatrixHelper() {
    }


    public static void perspectiveM(float[] m, float yFovInDegree, float aspect, float n, float f) {
        // 角度与弧度转换
        // 1弧度=(180/π)°角度
        // 1角度=π/180弧度
        final float angleInRadians = (float) (yFovInDegree * Math.PI / 180.0);
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        //       a: 以相机拍照为场景: a 就表示相机焦距, 焦距计算方式: 1/tan(视野/2), 该视野值必须小于 180 degrees
        //  aspect: 屏幕的宽高比 计算方式 宽度/高度
        //       f: 到远处平面距离, 必须为正值,且大于近处平面距离
        //       n: 到近距离平面的距离,必须为正值。 例如改值为 1 则远处平面就位于Z值为 -1 处

        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }


}
