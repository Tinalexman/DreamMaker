package dream.ecs.components;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class Mesh extends Component
{
    public transient float[] vertices;
    public transient float[] textures;
    public transient float[] normals;
    public transient int[] indices;

    private final transient int VAO_ID;

    private final transient List<Integer> LIST_OF_VBOS;

    public static final transient int CUBE_MESH = 1;
    public static final transient int PLANE_MESH = 2;

    public Mesh(int meshType)
    {
        this.VAO_ID = glGenVertexArrays();
        this.LIST_OF_VBOS = new ArrayList<>();
        setMesh(meshType);
    }

    public Mesh()
    {
        this.VAO_ID = glGenVertexArrays();
        this.LIST_OF_VBOS = new ArrayList<>();
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
        {
            if(this.vertices == null && this.textures == null && this.normals == null)
                setMesh(CUBE_MESH);
            load();
            this.ready = true;
        }
    }

    @Override
    public void onDestroyRequested()
    {
        for(int vbo : LIST_OF_VBOS)
            glDeleteBuffers(vbo);
        glDeleteVertexArrays(this.VAO_ID);
    }

    public void setMesh(int meshFlag)
    {
        if(meshFlag == CUBE_MESH)
            setCubeMesh();
        else if(meshFlag == PLANE_MESH)
            setPlaneMesh();
        enable();
    }

    private void setPlaneMesh()
    {
        this.vertices = new float[]
                {
                        -0.5f, -0.5f, -0.5f,
                        0.5f, -0.5f, -0.5f,
                        0.5f, 0.5f, -0.5f,
                        0.5f, 0.5f, -0.5f,
                        -0.5f, 0.5f, -0.5f,
                        -0.5f, -0.5f, -0.5f,

                        -0.5f, -0.5f, 0.5f,
                        0.5f, -0.5f, 0.5f,
                        0.5f, 0.5f, 0.5f,
                        0.5f, 0.5f, 0.5f,
                        -0.5f, 0.5f, 0.5f,
                        -0.5f, -0.5f, 0.5f,

                        -0.5f, 0.5f, 0.5f,
                        -0.5f, 0.5f, -0.5f,
                        -0.5f, -0.5f, -0.5f,
                        -0.5f, -0.5f, -0.5f,
                        -0.5f, -0.5f, 0.5f,
                        -0.5f, 0.5f, 0.5f,

                        0.5f, 0.5f, 0.5f,
                        0.5f, 0.5f, -0.5f,
                        0.5f, -0.5f, -0.5f,
                        0.5f, -0.5f, -0.5f,
                        0.5f, -0.5f, 0.5f,
                        0.5f, 0.5f, 0.5f,

                        -0.5f, -0.5f, -0.5f,
                        0.5f, -0.5f, -0.5f,
                        0.5f, -0.5f, 0.5f,
                        0.5f, -0.5f, 0.5f,
                        -0.5f, -0.5f, 0.5f,
                        -0.5f, -0.5f, -0.5f,

                        -0.5f, 0.5f, -0.5f,
                        0.5f, 0.5f, -0.5f,
                        0.5f, 0.5f, 0.5f,
                        0.5f, 0.5f, 0.5f,
                        -0.5f, 0.5f, 0.5f,
                        -0.5f, 0.5f, -0.5f
                };

        this.textures = new float[]
                {
                        0.0f, 0.0f,
                        1.0f, 0.0f,
                        1.0f, 1.0f,
                        1.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 0.0f,

                        0.0f, 0.0f,
                        1.0f, 0.0f,
                        1.0f, 1.0f,
                        1.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 0.0f,

                        1.0f, 0.0f,
                        1.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 0.0f,
                        1.0f, 0.0f,

                        1.0f, 0.0f,
                        1.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 0.0f,
                        1.0f, 0.0f,

                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,
                        1.0f, 0.0f,
                        0.0f, 0.0f,
                        0.0f, 1.0f,

                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,
                        1.0f, 0.0f,
                        0.0f, 0.0f,
                        0.0f, 1.0f
                };

        this.normals = new float[]
                {
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,

                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,

                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,

                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,

                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,

                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f
                };
    }

    private void setCubeMesh()
    {
        this.vertices = new float[]
                {
                        -0.5f, -0.5f, -0.5f,
                        0.5f, -0.5f, -0.5f,
                        0.5f, 0.5f, -0.5f,
                        0.5f, 0.5f, -0.5f,
                        -0.5f, 0.5f, -0.5f,
                        -0.5f, -0.5f, -0.5f,

                        -0.5f, -0.5f, 0.5f,
                        0.5f, -0.5f, 0.5f,
                        0.5f, 0.5f, 0.5f,
                        0.5f, 0.5f, 0.5f,
                        -0.5f, 0.5f, 0.5f,
                        -0.5f, -0.5f, 0.5f,

                        -0.5f, 0.5f, 0.5f,
                        -0.5f, 0.5f, -0.5f,
                        -0.5f, -0.5f, -0.5f,
                        -0.5f, -0.5f, -0.5f,
                        -0.5f, -0.5f, 0.5f,
                        -0.5f, 0.5f, 0.5f,

                        0.5f, 0.5f, 0.5f,
                        0.5f, 0.5f, -0.5f,
                        0.5f, -0.5f, -0.5f,
                        0.5f, -0.5f, -0.5f,
                        0.5f, -0.5f, 0.5f,
                        0.5f, 0.5f, 0.5f,

                        -0.5f, -0.5f, -0.5f,
                        0.5f, -0.5f, -0.5f,
                        0.5f, -0.5f, 0.5f,
                        0.5f, -0.5f, 0.5f,
                        -0.5f, -0.5f, 0.5f,
                        -0.5f, -0.5f, -0.5f,

                        -0.5f, 0.5f, -0.5f,
                        0.5f, 0.5f, -0.5f,
                        0.5f, 0.5f, 0.5f,
                        0.5f, 0.5f, 0.5f,
                        -0.5f, 0.5f, 0.5f,
                        -0.5f, 0.5f, -0.5f
                };

        this.textures = new float[]
                {
                        0.0f, 0.0f,
                        1.0f, 0.0f,
                        1.0f, 1.0f,
                        1.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 0.0f,

                        0.0f, 0.0f,
                        1.0f, 0.0f,
                        1.0f, 1.0f,
                        1.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 0.0f,

                        1.0f, 0.0f,
                        1.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 0.0f,
                        1.0f, 0.0f,

                        1.0f, 0.0f,
                        1.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 0.0f,
                        1.0f, 0.0f,

                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,
                        1.0f, 0.0f,
                        0.0f, 0.0f,
                        0.0f, 1.0f,

                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,
                        1.0f, 0.0f,
                        0.0f, 0.0f,
                        0.0f, 1.0f
                };

        this.normals = new float[]
                {
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,

                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,

                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,

                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,

                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,

                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f
                };
    }


    public void enable()
    {
        glBindVertexArray(this.VAO_ID);
        if(this.vertices != null)
            glEnableVertexAttribArray(0);
        if(this.textures != null)
            glEnableVertexAttribArray(1);
        if(this.normals != null)
            glEnableVertexAttribArray(2);
    }

    public int getVertexCount()
    {
        if(indices != null)
            return this.indices.length;
        else
            return this.vertices.length / 3;
    }

    public void disable()
    {
        if(this.vertices != null)
            glDisableVertexAttribArray(0);
        if(this.textures != null)
            glDisableVertexAttribArray(1);
        if(this.normals != null)
            glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }


    public void load()
    {
        glBindVertexArray(this.VAO_ID);
        if(this.vertices != null)
            createVBO(0, 3, this.vertices);
        if(this.textures  != null)
            createVBO(1, 2, this.textures);
        if(this.normals != null)
            createVBO(2, 3, this.normals);
        if(this.indices != null)
            createEBO(this.indices);
        disable();
    }

    private void createEBO(int[] data)
    {
        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        IntBuffer buffer = createIntBuffer(data);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    private void createVBO(int location, int size, float[] data)
    {
        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = createFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(location, size, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(location);
        MemoryUtil.memFree(buffer);
        this.LIST_OF_VBOS.add(vboID);
    }

    private static FloatBuffer createFloatBuffer(float[] coordinates)
    {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(coordinates.length);
        buffer.put(coordinates).flip();
        return buffer;
    }

    private static IntBuffer createIntBuffer(int[] coordinates)
    {
        IntBuffer buffer = MemoryUtil.memAllocInt(coordinates.length);
        buffer.put(coordinates).flip();
        return buffer;
    }

}
