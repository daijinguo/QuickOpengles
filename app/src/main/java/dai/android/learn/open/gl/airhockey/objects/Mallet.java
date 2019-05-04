package dai.android.learn.open.gl.airhockey.objects;

import android.opengl.GLES20;

import dai.android.learn.open.gl.airhockey.program.ColorShaderProgram;
import dai.android.learn.open.gl.misc.VertexArray;

import static dai.android.learn.open.gl.misc.Constants.BYTES_PER_FLOAT;

public class Mallet {
    private final static int POSITION_COMPONE_COUNT = 2;
    private static final int COLOR_COMPONE_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONE_COUNT + COLOR_COMPONE_COUNT) * BYTES_PER_FLOAT;


    private static final float[] VertexData = {
            // Order of coordinates: X, Y, R, G, B
            0f, -0.4f, 0f, 0f, 1f,
            0f,  0.4f, 1f, 0f, 0f
    };

    private final VertexArray mVertexArray;

    public Mallet() {
        mVertexArray = new VertexArray(VertexData);
    }

    public void bindData(ColorShaderProgram program) {
        mVertexArray.setVertexAttributePointer(
                0,
                program.getPositionAttributeLocation(),
                POSITION_COMPONE_COUNT,
                STRIDE);

        mVertexArray.setVertexAttributePointer(
                POSITION_COMPONE_COUNT,
                program.getColorAttributeLocation(),
                COLOR_COMPONE_COUNT,
                STRIDE);
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 2);
    }

}
