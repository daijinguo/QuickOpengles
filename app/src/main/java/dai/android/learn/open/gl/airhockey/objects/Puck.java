package dai.android.learn.open.gl.airhockey.objects;

import java.util.List;

import dai.android.learn.open.gl.airhockey.program.ColorShaderProgram;
import dai.android.learn.open.gl.misc.VertexArray;
import dai.android.utility.Geometry;

// 冰球
public class Puck {

    private static final int POSITION_COMPONENT_COUNT = 3;


    public final float Radius;
    public final float Height;

    private final VertexArray mVertexArray;
    private final List<IDrawCommand> mDrawer;

    public Puck(float radius, float height, int numPointsAroundPuck) {
        Radius = radius;
        Height = height;

        ObjectBuilder.GeneratedData data = ObjectBuilder.createPuck(
                new Geometry.Cylinder(new Geometry.Point(0f, 0f, 0f), radius, height),
                numPointsAroundPuck);

        mVertexArray = new VertexArray(data.VertexData);
        mDrawer = data.DrawList;
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
