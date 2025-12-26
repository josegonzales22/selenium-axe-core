package annotations;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Tag("e2e")
@Retention(value = RetentionPolicy.RUNTIME)
public @interface E2E {
}
