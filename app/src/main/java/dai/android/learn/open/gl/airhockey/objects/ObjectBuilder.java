package dai.android.learn.open.gl.airhockey.objects;

import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.List;

import dai.android.utility.Geometry;

class ObjectBuilder {
    private static final int FLOAT_PER_VERTEX = 3;
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private final float[] mVertexData;
    private final List<IDrawCommand> mDrawers = new ArrayList<>();
    private int mOffset = 0;

    private ObjectBuilder(int sizeInVertices) {
        mVertexData = new float[sizeInVertices * FLOAT_PER_VERTEX];
    }

    private void appendCircle(Geometry.Circle circle, int numPoints) {
        final int startVertex = mOffset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);

        // Center point of fan
        mVertexData[mOffset++] = circle.Center.X;
        mVertexData[mOffset++] = circle.Center.Y;
        mVertexData[mOffset++] = circle.Center.Z;

        for (int i = 0; i <= numPoints; ++i) {
            // 每个三角形与圆心夹角弧度
            float angleInRadians = ((float) i / (float) numPoints) * ((float) Math.PI * 2F);

            mVertexData[mOffset++] = circle.Center.X + circle.Radius * (float) Math.cos(angleInRadians);
            mVertexData[mOffset++] = circle.Center.Y;
            mVertexData[mOffset++] = circle.Center.Z + circle.Radius * (float) Math.sin(angleInRadians);
        }

        mDrawers.add(new IDrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });
    }

    private void appendOpenCylinder(Geometry.Cylinder cylinder, int numPoints) {
        final int startVertex = mOffset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
        final float yStart = cylinder.Center.Y - (cylinder.Height / 2f);
        final float yEnd = cylinder.Center.Y + (cylinder.Height / 2f);

        // Generate strip around center point. <= is used because we want to
        // generate the points at the starting angle twice, to complete the
        // strip.
        for (int i = 0; i <= numPoints; i++) {
            float angleInRadians = ((float) i / (float) numPoints) * ((float) Math.PI * 2f);

            float xPosition = cylinder.Center.X + cylinder.Radius * (float) Math.cos(angleInRadians);
            float zPosition = cylinder.Center.Z + cylinder.Radius * (float) Math.sin(angleInRadians);

            mVertexData[mOffset++] = xPosition;
            mVertexData[mOffset++] = yStart;
            mVertexData[mOffset++] = zPosition;

            mVertexData[mOffset++] = xPosition;
            mVertexData[mOffset++] = yEnd;
            mVertexData[mOffset++] = zPosition;
        }
        mDrawers.add(new IDrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });
    }

    private GeneratedData build() {
        return new GeneratedData(mVertexData, mDrawers);
    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // 下面两个函数说明见: ObjectBuilder.jpg 说明
    private static int sizeOfCircleInVertices(int numPoints) {
        return 1 + (numPoints + 1);
    }

    private static int sizeOfOpenCylinderInVertices(int numPoints) {
        return (numPoints + 1) * 2;
    }

    static class GeneratedData {
        final float[] VertexData;
        final List<IDrawCommand> DrawList;

        GeneratedData(float[] vertexData, List<IDrawCommand> drawer) {
            VertexData = vertexData;
            DrawList = drawer;
        }
    }

}
