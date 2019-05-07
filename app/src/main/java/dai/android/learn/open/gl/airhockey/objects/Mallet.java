package dai.android.learn.open.gl.airhockey.objects;

import java.util.List;

import dai.android.learn.open.gl.airhockey.program.ColorShaderProgram;
import dai.android.learn.open.gl.misc.VertexArray;
import dai.android.utility.Geometry;

public class Mallet {
    private final static int POSITION_COMPONENT_COUNT = 3;

    public final float Radius;
    public final float Height;


    private final VertexArray mVertexArray;
    private final List<IDrawCommand> mDrawer;

    public Mallet(float radius, float height, int numPointsAroundMallet) {
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createMallet(
                new Geometry.Point(0f, 0f, 0f),
                radius,
                height,
                numPointsAroundMallet);

        Radius = radius;
        Height = height;

        mVertexArray = new VertexArray(generatedData.VertexData);
        mDrawer = generatedData.DrawList;
    }

    public void bindData(ColorShaderProgram program) {
        mVertexArray.setVertexAttributePointer(
                0,
                program.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                0);
    }

    public void draw() {
        for (IDrawCommand cmd : mDrawer) {
            cmd.draw();
        }
    }

}
