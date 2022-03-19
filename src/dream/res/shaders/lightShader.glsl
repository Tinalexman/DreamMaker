#type VERTEX
#version 330 core

layout(location = 0) in vec3 vertices;
layout(location = 1) in vec2 textures;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec2 textureCoordinates;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(vertices, 1.0);
    textureCoordinates = textures;
}

#type FRAGMENT
#version 330 core

struct Material
{
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};


out vec4 fragColor;
in vec2 textureCoordinates;
uniform sampler2D sampler;

void main()
{
    fragColor = texture(sampler, textureCoordinates);
}