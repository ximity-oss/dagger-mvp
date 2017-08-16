package net.ximity.mvp;

import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

/**
 * Annotation processing utility class methods
 *
 * @author by Emarc Magtanong on 2017/08/16.
 */
public final class Util {

    private static Messager messager;
    private static Filer filer;

    public static void init(ProcessingEnvironment environment) {
        messager = environment.getMessager();
        filer = environment.getFiler();
    }

    public static void error(String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }

    public static void warn(String message) {
        messager.printMessage(Diagnostic.Kind.WARNING, message);
    }

    public static void note(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    public static void writeJavaFile(JavaFile file, String generatedFile) {
        if (!isEmpty(generatedFile)) Util.note("Generating " + generatedFile + "...");
        try {
            file.writeTo(filer);
            if (!isEmpty(generatedFile)) Util.note("Generated " + generatedFile);
        } catch (IOException e) {
            if (!isEmpty(generatedFile)) Util.warn("Unable to generate file for " + generatedFile + "!");
        }
    }

    private Util() {
    }
}
