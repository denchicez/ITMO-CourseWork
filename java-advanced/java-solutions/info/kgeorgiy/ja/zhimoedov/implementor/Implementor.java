package info.kgeorgiy.ja.zhimoedov.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

/**
 * Implementation class of {@link JarImpler} interface
 *
 * @author denchicez
 * @since 11
 */
public class Implementor implements JarImpler {
    /**
     * The value is defined of tabulation
     */
    private final static String TAB = "\t";

    /**
     * The value is defined of new line
     */
    private final static String NEW_LINE = System.lineSeparator();

    /**
     * Returns a string whose value is the concatenation of tab repeated {@code count} times.
     * <p>
     * If this string is empty or count is zero then the empty
     * string is returned.
     *
     * @param count number of times to repeat tab
     * @return A string composed of tab repeated
     * {@code count} times or the empty string if this
     * string is empty or count is zero
     * @throws IllegalArgumentException if the {@code count} is
     *                                  negative.
     * @since 11
     */
    private static String manyLines(int count) {
        return NEW_LINE.repeat(count);
    }

    /**
     * Returns a string whose value is the concatenation of line separator repeated {@code count} times.
     * <p>
     * If this string is empty or count is zero then the empty
     * string is returned.
     *
     * @param count number of times to repeat line separator
     * @return A string composed of tab repeated
     * {@code count} times or the empty string if this
     * string is empty or count is zero
     * @throws IllegalArgumentException if the {@code count} is
     *                                  negative.
     * @since 11
     */
    private static String manyTabs(int count) {
        return TAB.repeat(count);
    }

    /**
     * Returns a string that replaces dollars to dots.
     *
     * @param s the string in which the $ signs are replaced with dots
     * @return A string where $ replace to dots in {@code s} string
     * @since 1.4
     */
    private static String replaceDollarsString(String s) {
        return s.replaceAll("\\$", ".");
    }

    /**
     * Create directories up to {@code path}.
     * Creates paths to the folder so that you can create a {@code path}.
     * If for some reason it does not create a folder, it will give an error {@code ImplerException}.
     *
     * @param path the path to the file to which we want to create directories.
     * @throws ImplerException If you can't create directories for parent of {@code path}
     */
    private static void createDirectories(Path path) throws ImplerException {
        Path parent;
        if ((parent = path.getParent()) != null) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new ImplerException("Can't create output file", e);
            }
        }
    }

    /**
     * Returns a string to use the package from the interface we want to inherit from.
     * If the file does not have a package, then the method will return an empty string.
     *
     * @param token the interface from which we want to take the packet
     * @return A string of package of {@code token}. If {@code token} haven't package return empty string.
     * @since 9
     */
    private static String getPackage(Class<?> token) {
        if (token.getPackageName().equals("")) return "";
        return "package " + token.getPackageName() + ";";
    }

    /**
     * Returns a string that specifies the name of the class.
     * Is inherited from the interface, also opens the parenthesis for the beginning of the class
     *
     * @param token the passed interface from which we should inherit
     * @return header for declaring a class that inherits from the interface given by the {@code token}
     * @since 1.5
     */
    private static String getClassName(Class<?> token) {
        return "public class " + token.getSimpleName() + "Impl implements " +
                replaceDollarsString(token.getCanonicalName()) + " {";
    }

    /**
     * Returns application for ad the generic types needed to declare the method.
     * If not in the method, no generations are used, it returns an empty string
     *
     * @param listTypeParameters array of generic types for method
     * @return Returns parameter type string for generics
     * @since 1.8
     */
    private static String getParametersTypeMethod(TypeVariable<Method>[] listTypeParameters) {
        StringJoiner parametrs = new StringJoiner(", ", "<", "> ");
        for (TypeVariable<?> paramType : listTypeParameters) {
            parametrs.add(paramType.getTypeName());
        }
        if (parametrs.length() != 3) {
            return parametrs.toString();
        } else {
            return " ";
        }
    }

    /**
     * Returns a string that describes the {@code parameter} (its type and name) as a code
     *
     * @param type      type of method
     * @param parameter parameter of method
     * @return A string which concat type of method and name of method
     * @since 1.8
     */
    private static String getParam(Type type, Parameter parameter) {
        return replaceDollarsString(type.getTypeName()) + " " + parameter.getName();
    }

    /**
     * Returns {@link StringJoiner} parameters in method.
     * For all parameters in the method calls {@link  Implementor#getParam} after
     * which it adds up and issues as all parameters for the {@code method}
     *
     * @param method method which we need to implement
     * @return All {@code  method} parameters as a {@link StringJoiner} in code
     * @since 1.8
     */
    private static StringJoiner getParams(Method method) {
        Parameter[] params = method.getParameters();
        Type[] paramsTypes = method.getGenericParameterTypes();
        StringJoiner writeParameters = new StringJoiner(", ", "(", ")");
        for (int i = 0; i < params.length; i++) {
            writeParameters.add(getParam(paramsTypes[i], params[i]));
        }
        return writeParameters;
    }

    /**
     * Returns string of default value of method.
     * Returns an empty string for all strings, returns false for all booleans, returns 0 for all primitives,
     * null for others
     *
     * @param token method which we need to implement
     * @return A string of default value of {@code token}.
     */
    private static String getReturnValue(Class<?> token) {
        if (token.equals(boolean.class)) {
            return " false";
        } else if (token.equals(void.class)) {
            return "";
        } else if (token.isPrimitive()) {
            return " 0";
        }
        return " null";
    }


    /**
     * Returns {@link  StringBuilder} which describes {@code method}.
     * Concat {@link  Implementor#getParametersTypeMethod}, return type, method name,
     * {@link  Implementor#getParams}, {@link  Implementor#getReturnValue}
     * also arranges special to maintain java style. removes abstract,
     * native, transient for method modifiers.
     *
     * @param method method which we need to implement
     * @return A string to declare the method as code
     * @since 1.5
     */
    private static StringBuilder getMethod(Method method) {
        StringBuilder sb = new StringBuilder();
        final int mods = method.getModifiers() & ~Modifier.ABSTRACT & ~Modifier.NATIVE & ~Modifier.TRANSIENT;
        String modifiers = Modifier.toString(mods);
        String typeParametrs = getParametersTypeMethod(method.getTypeParameters());
        String typeReturn = replaceDollarsString(method.getGenericReturnType().getTypeName());
        String methodName = " " + method.getName();
        StringJoiner methodParams = getParams(method);
        // handle method
        sb.append(NEW_LINE).append(TAB).append(modifiers);
        sb.append(typeParametrs).append(typeReturn).append(methodName).append(methodParams).append(" {");
        // body method
        sb.append(NEW_LINE).append(manyTabs(2));
        sb.append("return").append(getReturnValue(method.getReturnType())).append(";");
        // end method
        sb.append(NEW_LINE).append(TAB).append("}").append(NEW_LINE);
        return sb;
    }

    /**
     * writes all methods for {@code token}.
     * Iterates through all {@code token} methods, calls {@link  Implementor#getMethod} and writes with {@code writer}
     *
     * @param writer to write to a class file that inherits from {@code token}
     * @param token  interface from which we want to inherit
     * @throws IOException If you can't create directories for parent of {@code path}
     * @since 1.5
     */
    private static void writeBody(BufferedWriter writer, Class<?> token) throws IOException {
        for (Method method : token.getMethods()) {
            writer.write(getMethod(method).toString());
        }
    }

    /**
     * Returns {@see StringBuilder} which describe {@code token}
     * Writes with the help {@code writer} to the file a class that is inherited from the
     * interface {@code token} as code
     *
     * @param token  interface from which we want to inherit
     * @param writer to write to a class file that inherits from {@code token}
     * @throws IOException If you can't create directories for parent of {@code path}
     * @since 1.5
     */
    private static void writeClassByToken(BufferedWriter writer, Class<?> token) throws IOException {
        String packageToken = getPackage(token);
        if (!packageToken.equals("")) {
            writer.write(getPackage(token));
            writer.write(manyLines(2));
        }
        writer.write(getClassName(token));
        writeBody(writer, token);
        writer.write("}");
    }

    /**
     * Script entry point for implementation relative to record and file token
     * Takes two or three arguments. If there are two arguments, then calls implement
     * to create a class from the first argument into a file from the second.
     * If three arguments are received, then it creates a .jar with the same conditions as in
     * the first case. It also checks for errors. Calls {@link Implementor#implement} and
     * {@link Implementor#implementJar} for create implements class on .java or .jar.
     *
     * @param args command line arguments
     * @since 11
     */
    public static void main(String[] args) {
        if (args == null) {
            System.err.println("Arguments must be NOT null");
        } else if (args.length != 2 && args.length != 3) {
            System.err.println("Need 2 or 3 arguments");
        } else {
            if (args.length == 3 && !Objects.equals(args[0], "-jar")) {
                System.err.println("First argument must be -jar");
                return;
            }
            if (args.length == 3 && !args[2].endsWith(".jar")) {
                System.err.println("Third argument must be end on .jar");
                return;
            }
            try {
                Class<?> token;
                if (args.length == 3) {
                    token = Class.forName(args[1]);
                } else {
                    token = Class.forName(args[0]);
                }
                try {
                    Implementor implementor = new Implementor();
                    if (args.length == 3) {
                        implementor.implementJar(token, Path.of(args[2]));
                    } else {
                        implementor.implement(token, Path.of(args[1]));
                    }
                } catch (InvalidPathException e) {
                    System.err.println("Can't get path from second arg");
                }
            } catch (InvalidPathException e) {
                System.err.println("Can't get path from first arg");
            } catch (ImplerException e) {
                System.err.println("Can't impler this interface");
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found");
            }
        }
    }

    /**
     * Returns a string that the name of the returned class.
     *
     * @param token interface from which we want to inherit
     * @return a string that describes the path to the file
     * @since 9
     */
    private static String getImplName(final Class<?> token) {
        return token.getPackageName() + "." + token.getSimpleName() + "Impl";
    }

    /**
     * Returns a string that describes the path to the file.
     *
     * @param root  path to the file relative to which we want to get the full path
     * @param clazz interface from which we want to inherit
     * @return a string that describes the path to the file
     * @since 9
     */
    public static Path getFile(final Path root, final Class<?> clazz) {
        return root.resolve(getImplName(clazz).replace(".", File.separator) + ".java").toAbsolutePath();
    }

    /**
     * Returns the full path to the interface.
     *
     * @param token interface from which we want to inherit
     * @return string of the full path to the interface
     * @since 9
     */
    private static String getClassPath(final Class<?> token) {
        try {
            return Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (final URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Сompiles the file by {@code token} and {@code root}.
     *
     * @param token interface from which we want to inherit
     * @param root  path to save compiler file
     * @since 9
     */
    public static void compileFile(final Class<?> token, final Path root) {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final String classpath = root + File.pathSeparator + getClassPath(token);
        final String[] args = {getFile(root, token).toString(), "-cp", classpath, "-encoding", "UTF-8"};
        compiler.run(null, null, null, args);
    }

    /**
     * Returns {@code root} resolve path to {@code token}.
     *
     * @param token     token interface from which we want to inherit
     * @param root      path to save {@code extension} file
     * @param extension extension file (can be .java or .class)
     * @return path to Impl .class or .java
     * @since 9
     */
    private Path getFilePath(Class<?> token, Path root, String extension) {
        return root.resolve(token.getPackageName().replace('.', File.separatorChar)).
                resolve(token.getSimpleName() + "Impl" + extension);
    }

    /**
     * Creates a class that inherits from the {@code token}, and writes it along the {@code root}.
     * The {@link Implementor#writeClassByToken} method writes a class to a file.
     * Then it checks for the correctness of the incoming data, if the transmitted data is null,
     * a private or primitive is passed, or the final interface throws errors
     *
     * @param token token interface from which we want to inherit
     * @param root  path to save .java file
     * @throws ImplerException If you can't create directories for parent of {@code path},
     *                         root or token is null, interface is private or primitive,
     *                         interface have private modifier.
     * @since 11
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        if (root == null || token == null) {
            throw new ImplerException("root and token must be not null");
        }
        if (Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("Is private interface");
        }
        if (token.isPrimitive() || Modifier.isFinal(token.getModifiers())) {
            throw new ImplerException("Incorrect class token");
        }
        root = getFilePath(token, root, ".java");
        createDirectories(root);
        try (BufferedWriter writer = Files.newBufferedWriter(root, StandardCharsets.UTF_8)) {
            writeClassByToken(writer, token);
        } catch (IOException e) {
            throw new ImplerException("Can't write into output file", e);
        }
    }

    /**
     * Returns string which describe {@code method}
     * For an interface {@code token}, creates a .jar file along the {@code jarFile}.
     * Throws an {@code ImplerException} if an error occurs with to write.
     * First calls {@link Implementor#implement} to create a .java file,
     * then compiles them with {@link  Implementor#compileFile} and puts them inside the .jar file
     *
     * @param token   token interface from which we want to inherit
     * @param jarFile path to save .jar file
     * @throws ImplerException If you can't create directories for parent of {@code path} or .jar file is null
     * @since 11
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        if (jarFile == null) {
            throw new ImplerException("jarFile must be not null");
        }
        Path root = jarFile.getParent();
        implement(token, root);
        compileFile(token, root);
        Manifest manifest = new Manifest();
        Attributes attributes = manifest.getMainAttributes();
        attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try (JarOutputStream stream = new JarOutputStream(Files.newOutputStream(jarFile), manifest)) {
            ZipEntry ZipEntry = new ZipEntry(getImplName(token).replace(".", "/") + ".class");
            stream.putNextEntry(ZipEntry);
            Files.copy(getFilePath(token, root, ".class"), stream);
        } catch (IOException e) {
            System.err.println("Problem with jar file");
        }
    }
}