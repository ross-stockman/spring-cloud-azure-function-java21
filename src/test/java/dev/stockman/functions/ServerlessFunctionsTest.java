package dev.stockman.functions;

import com.microsoft.azure.functions.ExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

class ServerlessFunctionsTest {

    private final ServerlessFunctions systemUnderTest = new ServerlessFunctions();

    @Test
    void hello() {
        String result = systemUnderTest.hello().apply("Kilroy");
        assertThat(result).isEqualTo("Kilroy was here!");
    }

    @Test
    void time() {
        String result = systemUnderTest.time().get();
        assertThat(result).isEqualTo("The time is very late!");
    }

    @Test
    void publish() {
        systemUnderTest.publish().accept("Foobar");
    }
}