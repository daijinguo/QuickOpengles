uniform mat4 u_Matrix;

attribute vec4 a_Position;
attribute vec4 a_Color;

void main()
{
    gl_Position  = u_Matrix * a_Position;
}
