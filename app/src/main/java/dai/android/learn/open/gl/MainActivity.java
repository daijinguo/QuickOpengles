package dai.android.learn.open.gl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import dai.android.learn.open.gl.airhockey.AirHockeyRenderer;
import dai.android.utility.Misc;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView mGLSurfaceView;
    private boolean mHasRendererSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);
        if (Misc.supportGLESv2(this)) {
            mGLSurfaceView.setEGLContextClientVersion(2);

            // ??
            //mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

            mGLSurfaceView.setRenderer(new AirHockeyRenderer(this));
            mHasRendererSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
            return;
        }

        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mHasRendererSet) {
            mGLSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mHasRendererSet) {
            mGLSurfaceView.onResume();
        }
    }
}
