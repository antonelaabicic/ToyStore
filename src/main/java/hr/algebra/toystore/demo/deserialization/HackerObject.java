package hr.algebra.toystore.demo.deserialization;

import java.io.Serial;
import java.io.Serializable;

public class HackerObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String payload = "Malicious payload";

    @Override
    public String toString() {
        return payload;
    }
}