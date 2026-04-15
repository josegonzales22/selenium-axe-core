package annotations.browsers.firefox;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Tag("firefox_desktop")
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FirefoxDesktop {
}