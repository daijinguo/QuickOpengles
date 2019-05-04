package dai.android.learn.open.gl.airhockey.program;

import android.content.Context;
import android.opengl.GLES20;

import dai.android.learn.open.gl.R;

public class ColorShaderProgram extends ShaderProgram {

    // Uniform locations
    private final int location_uMatrix;

    // Attribute locations
    private final int location_aPosition;
    private final int location_aColor;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.shader_vertex_airhockey_simple, R.raw.shader_fragment_airhockey_simple);

        // Retrieve uniform locations for the shader program.
        location_uMatrix = GLES20.glGetUniformLocation(mProgram, U_MATRIX);

        location_aPosition = GLES20.glGetAttribLocation(mProgram, A_POSITION);
        location_aColor = GLES20.glGetAttribLocation(mProgram, A_COLOR);
    }

    public void setUniforms(float[] matrix) {
        // Pass the matrix into the shader program.
        GLES20.glUniformMatrix4fv(location_uMatrix, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation() {
        return location_aPosition;
    }

    public int getColorAttributeLocation() {
        return location_aColor;
    }
}
