package annotations.browsers.edge;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Tag("edge_tablet")
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EdgeTablet {
}