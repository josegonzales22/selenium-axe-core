package annotations.browsers.chrome;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Tag("chrome_mobile")
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ChromeMobile {
}