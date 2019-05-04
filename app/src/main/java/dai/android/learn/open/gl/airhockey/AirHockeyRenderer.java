package dai.android.learn.open.gl.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import dai.android.learn.open.gl.R;
import dai.android.learn.open.gl.airhockey.objects.Mallet;
import dai.android.learn.open.gl.airhockey.objects.Table;
import dai.android.learn.open.gl.airhockey.program.ColorShaderProgram;
import dai.android.learn.open.gl.airhockey.program.TextureShaderProgram;
import dai.android.utility.MatrixHelper;
import dai.android.utility.TextureHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = AirHockeyRenderer.class.getSimpleName();

    private final Context mContext;

    private final float[] mProjectMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    private Table mTable;
    private Mallet mMallet;

    private int mTexture;

    private TextureShaderProgram mTextureProgram;
    private ColorShaderProgram mColorProgram;

    public AirHockeyRenderer(Context context) {
        mContext = context;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // set background clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        mTable = new Table();
        mMallet = new Mallet();

        mTextureProgram = new TextureShaderProgram(mContext);
        mColorProgram = new ColorShaderProgram(mContext);

        mTexture = TextureHelper.loadTextureFromResource(mContext, R.drawable.air_hockey_surface);
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

        // Draw the table.
        mTextureProgram.useProgram();
        mTextureProgram.setUniforms(mProjectMatrix, mTexture);
        mTable.bindData(mTextureProgram);


        // Draw the mallets.
        mColorProgram.useProgram();
        mColorProgram.setUniforms(mProjectMatrix);
        mMallet.bindData(mColorProgram);
        mMallet.draw();
    }
}
