package dai.android.learn.open.gl.airhockey.program;

import android.content.Context;
import android.opengl.GLES20;

import dai.android.learn.open.gl.R;

public class TextureShaderProgram extends ShaderProgram {
    // Uniform locations
    private final int location_uMatrix;
    private final int location_uTextureUnit;

    // Attribute locations
    private final int location_aPosition;
    private final int location_aTextureCoordinates;


    public TextureShaderProgram(Context context) {
        super(context, R.raw.shader_vertex_airhockey_texture, R.raw.shader_fragment_airhockey_texture);

        // Retrieve uniform locations for the shader program
        location_uMatrix = GLES20.glGetUniformLocation(mProgram, U_MATRIX);
        location_uTextureUnit = GLES20.glGetUniformLocation(mProgram, U_TEXTURE_UNIT);

        // Retrieve attribute locations for the shader program.
        location_aPosition = GLES20.glGetAttribLocation(mProgram, A_POSITION);
        location_aTextureCoordinates = GLES20.glGetAttribLocation(mProgram, A_TEXTURE_COORDINATES);
    }


    public void setUniforms(float[] matrix, int textureId) {
        // Pass the matrix into the shader program.
        GLES20.glUniformMatrix4fv(location_uMatrix, 1, false, matrix, 0);

        // Set the active texture unit to texture unit 0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        GLES20.glUniform1i(location_uTextureUnit, 0);
    }


    public int getPositionAttributeLocation() {
        return location_aPosition;
    }

    public int getTextureCoordinatesLocation() {
        return location_aTextureCoordinates;
    }
}
