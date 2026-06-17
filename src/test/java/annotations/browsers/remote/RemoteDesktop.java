package annotations.browsers.remote;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Tag("remote_desktop")
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RemoteDesktop {
}