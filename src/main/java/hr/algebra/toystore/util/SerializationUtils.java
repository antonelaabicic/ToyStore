package hr.algebra.toystore.util;

import hr.algebra.toystore.demo.deserialization.ToySnapshot;
import hr.algebra.toystore.demo.deserialization.WhitelistObjectInputStream;

import java.io.*;

public final class SerializationUtils {

    private static final String DIRECTORY = "serialized";
    private static final byte[] MAGIC_BYTES = { (byte) 0xAC, (byte) 0xED, 0x00, 0x05 };

    private SerializationUtils() {
        throw new UnsupportedOperationException("SerializationUtils is a utility class.");
    }

    public static void serialize(Object object, String filename) throws IOException {
        File directory = new File(DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, filename);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(object);
        }
    }

    public static ToySnapshot deserialize(String filename) throws IOException, ClassNotFoundException {
        File file = new File(DIRECTORY, filename);
        validateMagicBytes(file);

        try (WhitelistObjectInputStream in = new WhitelistObjectInputStream(new FileInputStream(file))) {
            return (ToySnapshot) in.readObject();
        }
    }

    public static void validateMagicBytes(File file) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            byte[] header = new byte[MAGIC_BYTES.length];
            int bytesRead = input.read(header);

            if (bytesRead != MAGIC_BYTES.length) {
                throw new StreamCorruptedException("File is too short to be a valid serialized object.");
            }

            for (int i = 0; i < MAGIC_BYTES.length; i++) {
                if (header[i] != MAGIC_BYTES[i]) {
                    throw new StreamCorruptedException("Invalid Java serialization header.");
                }
            }
        }
    }
}