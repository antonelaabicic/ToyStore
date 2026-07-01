package hr.algebra.toystore.util;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

public final class UrlValidator {
    private UrlValidator() {
        throw new UnsupportedOperationException("UrlValidator is a utility class.");
    }

    public static void validate(String url) {
        URI uri = parse(url);
        validateScheme(uri);
        validateHost(uri);
    }

    private static URI parse(String url) {
        try {
            return URI.create(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL.");
        }
    }

    private static void validateScheme(URI uri) {
        String scheme = uri.getScheme();
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            throw new IllegalArgumentException("Only HTTP and HTTPS are allowed.");
        }
    }

    private static void validateHost(URI uri) {
        try {
            InetAddress address = InetAddress.getByName(uri.getHost());
            if (address.isLoopbackAddress() || address.isSiteLocalAddress() || address.isLinkLocalAddress()
                    || address.isAnyLocalAddress() || address.isMulticastAddress()) {
                throw new IllegalArgumentException("Access to internal addresses is forbidden.");
            }
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Unknown host.");
        }
    }
}
