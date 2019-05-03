package dai.android.learn.open.gl.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.text.TextUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import dai.android.learn.open.gl.R;
import dai.android.utility.MatrixHelper;
import dai.android.utility.ShaderHelper;
import dai.android.utility.TextResourceReader;
import dai.android.utility.log.Logger;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = AirHockeyRenderer.class.getSimpleName();

    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";

    private static final int POSITION_COMPONENT_COUNT = 2;
    // private static final int POSITION_COMPONENT_COUNT = 4;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final Context mContext;

    private final FloatBuffer mVertexData;
    private final float[] mProjectMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    private int mProgramId;

    private int location_aPosition;
    private int location_aColor;
    private int location_uMatrix;

    public AirHockeyRenderer(Context context) {
        mContext = context;

        //
        // Vertex data is stored in the following manner:
        //
        // The first two numbers are part of the position: X, Y
        // The next three numbers are part of the color: R, G, B
        //
        float[] tableVerticesWithTriangles = {
                /// // Order of coordinates: X, Y, Z, W, R, G, B
                /// // Triangle Fan
                ///   0f,     0f, 0f, 1.5f,   1f,   1f,   1f,
                /// -0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,
                ///  0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,
                ///  0.5f,  0.8f, 0f,   2f, 0.7f, 0.7f, 0.7f,
                /// -0.5f,  0.8f, 0f,   2f, 0.7f, 0.7f, 0.7f,
                /// -0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,
                ///
                /// // Line 1
                /// -0.5f, 0f, 0f, 1.5f, 0f, 1f, 0f, 0f,
                ///  0.5f, 0f, 0f, 1.5f, 0f, 1f, 0f, 0f,
                ///
                /// // Mallets
                /// 0f, -0.4f, 0f, 1.25f, 0F, 0F, 1F,
                /// 0f,  0.4f, 0f, 1.75f, 1F, 0F, 0F


                // Order of coordinates: X, Y, R, G, B
                // Triangle Fan
                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

                // Line 1
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 1f, 0f, 0f,

                // Mallets
                0f, -0.4f, 0F, 0F, 1F,
                0f, 0.4f, 1F, 0F, 0F
        };

        mVertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexData.put(tableVerticesWithTriangles);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // set background clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderSource = TextResourceReader.readTextFileFromResource(
                mContext, R.raw.shader_vertex_airhockey_simple);
        if (TextUtils.isEmpty(vertexShaderSource)) {
            Logger.e(TAG, "bad vertex shader source");
            return;
        }

        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(
                mContext, R.raw.shader_fragment_airhockey_simple);
        if (TextUtils.isEmpty(fragmentShaderSource)) {
            Logger.e(TAG, "bad fragment shader source");
            return;
        }

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        if (vertexShader <= 0) {
            Logger.e(TAG, "can't compile vertex shader");
            return;
        }

        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        if (fragmentShader <= 0) {
            Logger.e(TAG, "can't compile fragment shader");
            return;
        }

        mProgramId = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        if (mProgramId <= 0) {
            Logger.e(TAG, "can't link vertex and fragment");
            return;
        }
        glUseProgram(mProgramId);


        location_aPosition = glGetAttribLocation(mProgramId, A_POSITION);
        location_aColor = glGetAttribLocation(mProgramId, A_COLOR);
        location_uMatrix = glGetUniformLocation(mProgramId, U_MATRIX);

        mVertexData.position(0);
        glVertexAttribPointer(location_aPosition, POSITION_COMPONENT_COUNT,
                GL_FLOAT, false, STRIDE, mVertexData);
        glEnableVertexAttribArray(location_aPosition);

        mVertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(location_aColor, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, mVertexData);
        glEnableVertexAttribArray(location_aColor);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(mProjectMatrix, 45, (float) width / (float) height, 1f, 10f);
        // MatrixHelper.perspectiveM maybe same as Matrix.perspectiveM
        //Matrix.perspectiveM(mProjectMatrix,0, 45, (float) width / (float) height, 1f, 10f);

        setIdentityM(mModelMatrix, 0);

        translateM(mModelMatrix, 0, 0f, 0f, -2.5f);
        rotateM(mModelMatrix, 0, -60f, 1f, 0f, 0f);

        final float[] temp = new float[16];
        multiplyMM(temp, 0, mProjectMatrix, 0, mModelMatrix, 0);
        System.arraycopy(temp, 0, mProjectMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        // Assign the matrix
        glUniformMatrix4fv(location_uMatrix, 1, false, mProjectMatrix, 0);

        // Draw the table.
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        // Draw the center dividing line.
        glDrawArrays(GL_LINES, 6, 2);


        // Draw the first mallet blue.
        glDrawArrays(GL_POINTS, 8, 1);


        // Draw the second mallet red.
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
