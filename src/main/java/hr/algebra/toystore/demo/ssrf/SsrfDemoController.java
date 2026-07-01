package hr.algebra.toystore.demo.ssrf;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo/ssrf")
@RequiredArgsConstructor
public class SsrfDemoController {

    private final SsrfDemoService ssrfDemoService;

    @GetMapping("/vulnerable")
    public String vulnerable(@RequestParam String url) {
        return ssrfDemoService.fetchVulnerable(url);
    }

    @GetMapping("/secure")
    public String secure(@RequestParam String url) {
        return ssrfDemoService.fetchSecure(url);
    }
}