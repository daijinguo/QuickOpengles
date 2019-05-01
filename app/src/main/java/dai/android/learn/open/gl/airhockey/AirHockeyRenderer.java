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
import dai.android.utility.ShaderHelper;
import dai.android.utility.TextResourceReader;
import dai.android.utility.log.Logger;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = AirHockeyRenderer.class.getSimpleName();

    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;

    private final Context mContext;

    private final FloatBuffer mVertexData;

    private int mProgramId;
    private int location_uColor;
    private int location_aPosition;

    public AirHockeyRenderer(Context context) {
        mContext = context;

        float[] tableVerticesWithTriangles = {
                // Triangle 1
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,

                // Triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,

                // Line 1
                -0.5f, 0f,
                0.5f, 0f,

                // Mallets
                0f, -0.25f,
                0f, 0.25f
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
        location_uColor = glGetUniformLocation(mProgramId, U_COLOR);

        mVertexData.position(0);
        glVertexAttribPointer(location_aPosition, POSITION_COMPONENT_COUNT,
                GL_FLOAT, false, 0, mVertexData);

        glEnableVertexAttribArray(location_aPosition);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        // Draw the table.
        glUniform4f(location_uColor, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 6);

        // Draw the center dividing line.
        glUniform4f(location_uColor, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);


        // Draw the first mallet blue.
        glUniform4f(location_uColor, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);


        // Draw the second mallet red.
        glUniform4f(location_uColor, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
