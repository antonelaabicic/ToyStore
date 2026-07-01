package hr.algebra.toystore.demo.deserialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Set;

public class WhitelistObjectInputStream extends ObjectInputStream {
    private static final Set<String> ALLOWED_CLASSES = Set.of(
            ToySnapshot.class.getName(),
            "java.lang.Double",
            "java.lang.Number"
    );

    public WhitelistObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String className = desc.getName();
        if (!ALLOWED_CLASSES.contains(className)) {
            throw new InvalidClassException("Deserialization of class '" + className + "' is not allowed.");
        }
        return super.resolveClass(desc);
    }
}