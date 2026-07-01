package hr.algebra.toystore.demo.deserialization;

import hr.algebra.toystore.util.SerializationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@RestController
@RequestMapping("/api/demo/deserialization")
@RequiredArgsConstructor
public class DeserializationDemoController {

    @PostMapping("/serialize")
    public String serialize(@RequestBody ToySnapshot toy) throws IOException {
        SerializationUtils.serialize(toy, "toy.ser");
        return "ToySnapshot serialized successfully.";
    }

    @GetMapping("/deserialize")
    public ToySnapshot deserialize() throws Exception {
        return SerializationUtils.deserialize("toy.ser");
    }

    @PostMapping("/serialize-hacker")
    public String serializeHacker() throws IOException {
        SerializationUtils.serialize(new HackerObject(), "hacker.ser");
        return "HackerObject serialized successfully.";
    }

    @GetMapping("/deserialize-hacker")
    public Object deserializeHacker() throws Exception {
        return SerializationUtils.deserialize("hacker.ser");
    }

    @PostMapping("/create-invalid")
    public String createInvalidFile() throws IOException {
        File directory = new File("serialized");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter writer = new FileWriter(new File(directory, "invalid.ser"))) {
            writer.write("This is not a Java serialized object.");
        }

        return "Invalid file created.";
    }

    @GetMapping("/deserialize-invalid")
    public Object deserializeInvalid() throws Exception {
        return SerializationUtils.deserialize("invalid.ser");
    }
}