package hr.algebra.toystore.demo.ssrf;

import hr.algebra.toystore.util.UrlValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SsrfDemoService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchVulnerable(String url) {
        return restTemplate.getForObject(url, String.class);
    }

    public String fetchSecure(String url) {
        UrlValidator.validate(url);
        return restTemplate.getForObject(url, String.class);
    }
}