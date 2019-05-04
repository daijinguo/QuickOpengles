package dai.android.learn.open.gl.misc;

import android.opengl.GLES20;
import android.support.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static dai.android.learn.open.gl.misc.Constants.BYTES_PER_FLOAT;

public class VertexArray {
    private final FloatBuffer mFloatBuffer;

    public VertexArray(@NonNull float[] vertexData) {
        mFloatBuffer = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    public void setVertexAttributePointer(int dataOffset, int attributeLocation, int componentCount, int stride) {
        mFloatBuffer.position(dataOffset);
        GLES20.glVertexAttribPointer(
                attributeLocation,
                componentCount,
                GLES20.GL_FLOAT,
                false,
                stride,
                mFloatBuffer);
        GLES20.glEnableVertexAttribArray(attributeLocation);

        mFloatBuffer.position(0);
    }
}
