package annotations.browsers.chrome;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Tag("chrome_desktop")
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ChromeDesktop {
}