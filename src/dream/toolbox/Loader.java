package dream.toolbox;

import dream.io.output.Window;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;

public final class Loader
{
    public static byte[] loadFromResources(final String filename)
    {
        try (InputStream is = Objects.requireNonNull(Window.class.getClassLoader().getResourceAsStream(filename));
             ByteArrayOutputStream buffer = new ByteArrayOutputStream())
        {
            final byte[] data = new byte[16384];
            int nRead;

            while ((nRead = is.read(data, 0, data.length)) != -1)
                buffer.write(data, 0, nRead);

            return buffer.toByteArray();
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    public static Matrix3f calculateInverseProjection(Matrix4f transformationMatrix)
    {
        transformationMatrix.invert();
        transformationMatrix.transpose();
        return new Matrix3f(transformationMatrix);
    }

}
