package dai.android.learn.open.gl.airhockey.objects;

import android.opengl.GLES20;

import dai.android.learn.open.gl.airhockey.program.TextureShaderProgram;
import dai.android.learn.open.gl.misc.VertexArray;

import static dai.android.learn.open.gl.misc.Constants.BYTES_PER_FLOAT;

public class Table {
    private static final int POSITION_COMPONET_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONET_COUNT = 2;
    private static final int STRIDE =
            (POSITION_COMPONET_COUNT + TEXTURE_COORDINATES_COMPONET_COUNT) * BYTES_PER_FLOAT;

    private static final float[] VertexData = {
            // Order of coordinates: X, Y, S, T

            // Triangle Fan
               0f,    0f, 0.5f, 0.5f,
            -0.5f, -0.8f,   0f, 0.9f,
             0.5f, -0.8f,   1f, 0.9f,
             0.5f,  0.8f,   1f, 0.1f,
            -0.5f,  0.8f,   0f, 0.1f,
            -0.5f, -0.8f,   0f, 0.9f
    };

    private final VertexArray mVertexArray;

    public Table() {
        mVertexArray = new VertexArray(VertexData);
    }

    public void bindData(TextureShaderProgram textureProgram) {
        mVertexArray.setVertexAttributePointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONET_COUNT,
                STRIDE);

        mVertexArray.setVertexAttributePointer(
                POSITION_COMPONET_COUNT,
                textureProgram.getTextureCoordinatesLocation(),
                TEXTURE_COORDINATES_COMPONET_COUNT,
                STRIDE);
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
    }
}
