package dai.android.learn.open.gl.airhockey.program;

import android.content.Context;
import android.opengl.GLES20;

import dai.android.utility.ShaderHelper;
import dai.android.utility.TextResourceReader;

abstract class ShaderProgram {
    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_COLOR = "u_Color";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    // Shader program
    protected final int mProgram;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        mProgram = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId)
        );
    }

    public void useProgram() {
        GLES20.glUseProgram(mProgram);
    }

}
