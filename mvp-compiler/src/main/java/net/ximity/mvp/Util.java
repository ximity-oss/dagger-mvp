package net.ximity.mvp;

import java.util.Collection;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

/**
 * Annotation processing utility class methods
 *
 * @author by Emarc Magtanong on 2017/08/16.
 */
public final class Util {

    private static ProcessingEnvironment processingEnv;

    public static void init(ProcessingEnvironment environment) {
        processingEnv = environment;
    }

    public static void error(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }

    public static void warn(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, message);
    }

    public static void note(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    private Util() {
    }
}
