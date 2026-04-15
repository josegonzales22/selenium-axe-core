package annotations.browsers.firefox;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Tag("firefox_mobile")
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FirefoxMobile {
}