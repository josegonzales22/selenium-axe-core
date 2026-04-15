package annotations.browsers.edge;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Tag("edge_mobile")
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EdgeMobile {
}