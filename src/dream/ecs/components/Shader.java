package dream.ecs.components;

import dream.light.DirectionalLight;
import dream.ecs.entities.EntityManager;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader extends Component
{
    private transient int vertexShader;
    private transient int fragmentShader;

    private transient int programID;

    private transient String vertexShaderSource;
    private transient String fragmentShaderSource;

    private transient String shaderFilePath;

    private transient Map<String, Integer> uniformLocations;

    public Shader()
    {
        this.shaderFilePath = "src/dream/res/shaders/defaultShader.glsl";
    }

    @Override
    public boolean isDrawable()
    {
        return false;
    }

    @Override
    public void onStart()
    {
       if(!this.ready)
           createShaderObject(this.shaderFilePath);
    }

    @Override
    public void onDestroyRequested()
    {
        stopProgram();
        if(this.programID != 0)
            glDeleteProgram(this.programID);
    }

    public Shader(String shaderFilePath)
    {
        this.shaderFilePath = shaderFilePath;
    }

    private void createShaderObject(String shaderFile)
    {
        loadShader(shaderFile);
        this.programID = glCreateProgram();
        if(programID == 0)
            throw new RuntimeException("Cannot create shader program!");

        this.vertexShader = createShader(GL_VERTEX_SHADER);
        this.fragmentShader = createShader(GL_FRAGMENT_SHADER);

        linkProgram();
        if(EntityManager.checkIfParented(this.parentID))
            EntityManager.getEntity(this.parentID).addComponent(new Material());

        this.uniformLocations = new HashMap<>();
        this.ready = true;
    }

    private int createShader(int shaderType)
    {
        int shaderID = glCreateShader(shaderType);
        if(shaderID == 0)
            throw new IllegalStateException("Cannot create shader!");

        glShaderSource(shaderID, (shaderType == GL_VERTEX_SHADER ? this.vertexShaderSource : this.fragmentShaderSource));
        glCompileShader(shaderID);

        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0)
            throw new IllegalStateException("Cannot compile shader");

        glAttachShader(this.programID, shaderID);
        return shaderID;
    }

    private void loadShader(String shaderFilePath)
    {
        try
        {
            String source = new String(Files.readAllBytes(Paths.get(shaderFilePath)));
            String[] splitShaders = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if(firstPattern.equalsIgnoreCase("vertex"))
                this.vertexShaderSource = splitShaders[1];
            else if(firstPattern.equalsIgnoreCase("fragment"))
                this.fragmentShaderSource = splitShaders[1];
            else
                throw new RuntimeException("Illegal Shader Type: '" + firstPattern + "'");

            if(secondPattern.equalsIgnoreCase("vertex"))
                this.vertexShaderSource = splitShaders[2];
            else if(secondPattern.equalsIgnoreCase("fragment"))
                this.fragmentShaderSource = splitShaders[2];
            else
                throw new RuntimeException("Illegal Shader Type: '" + secondPattern + "'");
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Cannot load shader: '" + shaderFilePath + "' due to " + ex.getMessage());
        }
    }

    private void linkProgram()
    {
        glLinkProgram(this.programID);
        if(glGetProgrami(this.programID, GL_LINK_STATUS) == 0)
        {
            String errorMessage = "Cannot link program: " + glGetProgramInfoLog(this.programID, 1024);
            throw new IllegalStateException(errorMessage);
        }

        if(this.vertexShader != 0)
            glDetachShader(this.programID, this.vertexShader);

        if(this.fragmentShader != 0)
            glDetachShader(this.programID, this.fragmentShader);

        glValidateProgram(this.programID);
        if(glGetProgrami(this.programID, GL_VALIDATE_STATUS) == 0)
            System.err.println("Warning! Shader code validation " + glGetProgramInfoLog(this.programID, 1024));
    }

    public void startProgram()
    {
        glUseProgram(this.programID);
    }

    public void stopProgram()
    {
        glUseProgram(0);
    }

    public void loadUniform(String name, boolean value) throws Exception
    {
        loadUniform(name, (value ? 1.0f : 0.0f));
    }

    public void loadUniform(String name, Vector3f value) throws Exception
    {
        loadUniform(name, value.x, value.y, value.z);
    }

    public void loadUniform(String name, float x, float y, float z) throws Exception
    {
        int location = uniformLocations.computeIfAbsent(name, integer -> glGetUniformLocation(this.programID, name));
        if(location < 0)
            throw new Exception("Cannot locate uniform: " + name);
        glUniform3f(location, x, y, z);
    }

    public void loadUniform(String name, float x, float y, float z, float w) throws Exception
    {
        int location = uniformLocations.computeIfAbsent(name, integer -> glGetUniformLocation(this.programID, name));
        if(location < 0)
            throw new Exception("Cannot locate uniform: " + name);
        glUniform4f(location, x, y, z, w);
    }

    public void loadUniform(String name, Vector4f value) throws Exception
    {
        loadUniform(name, value.x, value.y, value.z, value.w);
    }

    public void loadUniform(String name, float value) throws Exception
    {
        int location = uniformLocations.computeIfAbsent(name, integer -> glGetUniformLocation(this.programID, name));
        if(location < 0)
            throw new Exception("Cannot locate uniform: " + name);
        glUniform1f(location, value);
    }

    public void loadUniform(String name, int value) throws Exception
    {
        int location = uniformLocations.computeIfAbsent(name, integer -> glGetUniformLocation(this.programID, name));
        if(location < 0)
            throw new Exception("Cannot locate uniform: " + name);
        glUniform1i(location, value);
    }

    public void loadUniform(String name, Matrix3f value) throws Exception
    {
        int location = uniformLocations.computeIfAbsent(name, integer -> glGetUniformLocation(this.programID, name));
        if(location < 0)
            throw new Exception("Cannot locate uniform: " + name);
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            glUniformMatrix3fv(location, false, value.get(stack.mallocFloat(9)));
        }

//        FloatBuffer buffer = MemoryUtil.memAllocFloat(9);
//        float[] src = new float[9];
//        src[0] = value.m00();     src[1] = value.m01(); src[2] = value.m02();
//        src[3] = value.m10();     src[4] = value.m11(); src[5] = value.m12();
//        src[6] = value.m21();     src[7] = value.m21(); src[8] = value.m22();
//        buffer.put(src).flip();
//        glUniformMatrix3fv(location, false, buffer);
    }

    public void loadUniform(String name, Matrix4f value) throws Exception
    {
        int location = uniformLocations.computeIfAbsent(name, integer -> glGetUniformLocation(this.programID, name));
        if(location < 0)
            throw new Exception("Cannot locate uniform: " + name);
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            glUniformMatrix4fv(location, false, value.get(stack.mallocFloat(16)));
        }

//        FloatBuffer buffer = MemoryUtil.memAllocFloat(16);
//        float[] src = new float[16];
//        src[0] = value.m00();   src[1] = value.m01();   src[2] = value.m02();   src[3] = value.m03();
//        src[4] = value.m10();   src[5] = value.m11();   src[6] = value.m12();   src[7] = value.m13();
//        src[8] = value.m20();   src[9] = value.m21();   src[10] = value.m22();   src[11] = value.m23();
//        src[12] = value.m30();   src[13] = value.m31();   src[14] = value.m32();   src[15] = value.m33();
//        buffer.put(src).flip();
//
    }

    public void applyMaterial(String uniformName, Material material) throws Exception
    {
        loadUniform(uniformName + ".ambient", material.getAmbient());
        loadUniform(uniformName + ".diffuse", material.getDiffuse());
        loadUniform(uniformName + ".specular", material.getSpecular());
        loadUniform(uniformName + ".shininess", material.getShininess());
    }

    public void loadDirectionalLight(String uniformName, DirectionalLight light) throws Exception
    {
        loadUniform(uniformName + ".color", light.getColor());
        loadUniform(uniformName + ".direction", light.getDirection());
        loadUniform(uniformName + ".intensity", light.getIntensity());
        //loadUniform(uniformName + ".shininess", material.getShininess());
    }

}
