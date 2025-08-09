#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

uniform vec2 RectPos;
uniform vec2 RectSize;
uniform float Radius;

out vec4 vertexColor;
out vec2 texCoord;

out vec2 vRectPos;
out vec2 vRectSize;
out float vRadius;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    vertexColor = Color;
    texCoord = UV0;
    vRectPos = RectPos;
    vRectSize = RectSize;
    vRadius = Radius;
}
