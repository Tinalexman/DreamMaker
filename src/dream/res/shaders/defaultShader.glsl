#type VERTEX
#version 330 core

layout(location = 0) in vec3 vertices;
layout(location = 1) in vec2 textures;
layout(location = 2) in vec3 normals;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat3 inversedMatrix;

out vec2 textureCoordinates;
out vec3 fragPos;
out vec3 normalizedCoordinates;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(vertices, 1.0);
    fragPos = vec3(transformationMatrix * vec4(vertices, 1.0));
    textureCoordinates = textures;
    normalizedCoordinates = inversedMatrix * normals;
};


#type FRAGMENT
 #version 330 core

out vec4 outColor;

in vec3 fragPos;
in vec2 textureCoordinates;
in vec3 normalizedCoordinates;

uniform vec3 viewPosition;

struct Material
{
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

uniform Material material;

void main()
{
    vec3 ambient = vec3(1.0) * material.ambient;

    vec3 norm = normalize(normalizedCoordinates);
    vec3 lightDirection = normalize(vec3(5.0) - fragPos);
    float diff = max(dot(norm, lightDirection), 0.0);
    vec3 diffuse = vec3(1.0) * (diff * material.diffuse);

    vec3 viewDirection = normalize(vec3(0.0f, 0.0f, -5.0f) - fragPos);
    vec3 reflectDirection = reflect(-lightDirection, norm);
    float spec = pow(max(dot(viewDirection, reflectDirection), 0.0), material.shininess);
    vec3 specular = vec3(1.0) * (spec * material.specular);

    vec3 result = ambient + diffuse + specular;
    outColor = vec4(result, 1.0);
};