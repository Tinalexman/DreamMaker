#type VERTEX
#version 330 core

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textures;
layout (location = 2) in vec3 normals;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec3 Normal;
out vec3 fragPos;
out vec2 textureCoords;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(vertices, 1.0);
    fragPos = vec3(transformationMatrix * vec4(vertices, 1.0));
    Normal = mat3(transpose(inverse(transformationMatrix))) * normals;
    textureCoords = textures;
}

#type FRAGMENT
#version 330 core

out vec4 outColor;

in vec3 Normal;
in vec3 fragPos;
in vec2 textureCoords;

uniform vec3 viewPosition;

struct TexturedMaterial
{
    sampler2D diffuse;
    vec3 specular;
    float shininess;
};

struct RawMaterial
{
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

struct Light
{
    vec3 position;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

uniform RawMaterial rawMaterial;
uniform TexturedMaterial texturedMaterial;
uniform Light light;
uniform float isTextured;

void main()
{
    vec3 ambient;
    if(isTextured == 1.0)
        ambient = light.ambient * vec3(texture(texturedMaterial.diffuse, textureCoords));
    else
        ambient = light.ambient * rawMaterial.ambient;

    vec3 norm = normalize(Normal);
    vec3 lightDirection = normalize(light.position - fragPos);
    float diff = max(dot(norm, lightDirection), 0.0);
    vec3 diffuse;
    if(isTextured == 1.0)
        diffuse = light.diffuse * diff * vec3(texture(texturedMaterial.diffuse, textureCoords));
    else
        diffuse = light.diffuse * (diff * rawMaterial.diffuse);

    vec3 viewDirection = normalize(viewPosition - fragPos);
    vec3 reflectDirection = reflect(-lightDirection, norm);
    float spec = pow(max(dot(viewDirection, reflectDirection), 0.0), rawMaterial.shininess);
    vec3 specular = rawMaterial.specular * spec * light.specular;

    vec3 result = ambient + diffuse + specular;
    outColor = vec4(result, 1.0);
}