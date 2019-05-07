package dai.android.learn.open.gl.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import dai.android.learn.open.gl.R;
import dai.android.learn.open.gl.airhockey.objects.Mallet;
import dai.android.learn.open.gl.airhockey.objects.Puck;
import dai.android.learn.open.gl.airhockey.objects.Table;
import dai.android.learn.open.gl.airhockey.program.ColorShaderProgram;
import dai.android.learn.open.gl.airhockey.program.TextureShaderProgram;
import dai.android.utility.MatrixHelper;
import dai.android.utility.TextureHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.setLookAtM;

public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = AirHockeyRenderer.class.getSimpleName();

    private final Context mContext;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    private Table mTable;
    private Mallet mMallet;
    private Puck mPuck;

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
        mMallet = new Mallet(0.08f, 0.15f, 32);
        mPuck = new Puck(0.06f, 0.02f, 32);

        mTextureProgram = new TextureShaderProgram(mContext);
        mColorProgram = new ColorShaderProgram(mContext);

        mTexture = TextureHelper.loadTextureFromResource(mContext, R.drawable.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
        // MatrixHelper.perspectiveM maybe same as Matrix.perspectiveM
        //Matrix.perspectiveM(mProjectMatrix,0, 45, (float) width / (float) height, 1f, 10f);

        setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);


        // Multiply the view and projection matrices together.
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        // Draw the table.
        positionTableInScene();
        mTextureProgram.useProgram();
        mTextureProgram.setUniforms(modelViewProjectionMatrix, mTexture);
        mTable.bindData(mTextureProgram);
        mTable.draw();

        // Draw the mallets.
        positionObjectInScene(0f, mMallet.Height / 2f, -0.4f);
        mColorProgram.useProgram();
        mColorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
        mMallet.bindData(mColorProgram);
        mMallet.draw();

        positionObjectInScene(0f, mMallet.Height / 2f, 0.4f);
        mColorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
        // Note that we don't have to define the object data twice -- we just
        // draw the same mallet again but in a different position and with a
        // different color.
        mMallet.draw();


        // Draw the puck.
        positionObjectInScene(0f, mPuck.Height / 2f, 0f);
        mColorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        mPuck.bindData(mColorProgram);
        mPuck.draw();
    }


    private void positionTableInScene() {
        // The table is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    // The mallets and the puck are positioned on the same plane as the table.
    private void positionObjectInScene(float x, float y, float z) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, z);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }
}
