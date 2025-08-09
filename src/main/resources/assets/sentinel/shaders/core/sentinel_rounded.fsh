#version 150

in vec4 vertexColor;
in vec2 texCoord;

in vec2 vRectPos;
in vec2 vRectSize;
in float vRadius;

out vec4 FragColor;

void main() {
    vec2 p = gl_FragCoord.xy;

    vec2 bl = vRectPos;
    vec2 br = vRectPos + vec2(vRectSize.x, 0.0);
    vec2 tl = vRectPos + vec2(0.0, vRectSize.y);
    vec2 tr = vRectPos + vRectSize;

    if (p.x < bl.x + vRadius && p.y < bl.y + vRadius) {
        if (distance(p, bl + vec2(vRadius, vRadius)) > vRadius) discard;
    }
    if (p.x > br.x - vRadius && p.y < br.y + vRadius) {
        if (distance(p, br + vec2(-vRadius, vRadius)) > vRadius) discard;
    }
    if (p.x < tl.x + vRadius && p.y > tl.y - vRadius) {
        if (distance(p, tl + vec2(vRadius, -vRadius)) > vRadius) discard;
    }
    if (p.x > tr.x - vRadius && p.y > tr.y - vRadius) {
        if (distance(p, tr + vec2(-vRadius, -vRadius)) > vRadius) discard;
    }

    FragColor = vec4(vertexColor.rgb, 1.0);
}
