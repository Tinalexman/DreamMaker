package dream.scripting;

import javax.tools.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;

public class ScriptCompiler
{
    public static void compileScript(String scriptFile)
    {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println(scriptFile);
        out.close();

        JavaFileObject fileObject = new JavaSourceFromString("Script", writer.toString());
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(fileObject);

        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics,
                null, null, compilationUnits);

        boolean success = task.call();
        for(Diagnostic diagnostic : diagnostics.getDiagnostics())
        {
            System.out.println(diagnostic.getCode());
            System.out.println(diagnostic.getKind());
            System.out.println(diagnostic.getPosition());
            System.out.println(diagnostic.getStartPosition());
            System.out.println(diagnostic.getEndPosition());
            System.out.println(diagnostic.getSource());
            System.out.println(diagnostic.getMessage(null));
        }
        System.out.println("Success: " + success);



//        if(success)
//        {
//            try
//            {
//                Script script = (Script) Class.forName("dream.scripting.Script").cast(new Script());
//            }
//            catch (ClassNotFoundException ex)
//            {
//                System.err.println("Class Not Found: " + ex);
//            }
//            catch (NoSuchMethodException ex)
//            {
//                System.err.println("No Such Method: " + ex);
//            }
//            catch (IllegalAccessException ex)
//            {
//                System.err.println("Illegal Access Exception: " + ex);
//            }
//            catch (InvocationTargetException ex)
//            {
//                System.err.println("Invocation Target: " + ex);
//            }
//        }


    }

    static class JavaSourceFromString extends SimpleJavaFileObject
    {
        private final String code;

        public JavaSourceFromString(String name, String code)
        {
            super(URI.create("string://" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors)
        {
            return this.code;
        }
    }
}
