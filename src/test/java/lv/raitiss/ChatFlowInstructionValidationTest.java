package lv.raitiss;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ChatFlowInstructionValidationTest {
    @Test
    void shouldNotAcceptMissingMessages() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ChatFlow("./src/main/resources/instructions_missing_messages.json");
        });

        String expectedMessage = "Missing message";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldNotAcceptNoEnd() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ChatFlow("./src/main/resources/instructions_no_end.json");
        });

        String expectedMessage = "Instructions don't have a defined end";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldNotAcceptMismatchingPlaceholders() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ChatFlow("./src/main/resources/instructions_mismatching_placeholders.json");
        });

        String expectedMessage = "has no input";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
