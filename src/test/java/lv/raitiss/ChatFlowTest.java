package lv.raitiss;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatFlowTest {
    private ChatFlow chatFlow;

    @BeforeEach
    public void setUp() throws IOException {
        String path = "./src/main/resources/instructions_example.json";
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<ChatInstruction>> instructionList = new TypeReference<List<ChatInstruction>>() {
        };
        List<ChatInstruction> instructions = objectMapper.readValue(new File(path), instructionList);
        chatFlow = new ChatFlow(instructions);
    }

    @Test
    void shouldAcceptNameAndGender() {
        boolean continueChat = true;
        String[] inputs = {"John", "Doe", "male"};
        int inputCounter = 0;

        while (continueChat) {
            switch (chatFlow.getInstructionType()) {
                case MESSAGE -> chatFlow.readMessage();
                case INPUT -> chatFlow.enterInput(inputs[inputCounter++]);
                case CHOICE -> chatFlow.enterChoice(inputs[inputCounter++]);
                case END -> continueChat = false;
            }
        }

        HashMap<String, String> expectedValues = new HashMap<>();
        expectedValues.put("firstName", "John");
        expectedValues.put("lastName", "Doe");
        expectedValues.put("gender", "male");

        assertEquals(expectedValues, chatFlow.getUserVariables());
    }

    @Test
    void regexShouldAcceptLatvianNames() {
        boolean continueChat = true;
        String[] inputs = {"Mārtiņš Miķelis", "Šķūriņš-Čužāns", "male"};
        int inputCounter = 0;

        while (continueChat) {
            switch (chatFlow.getInstructionType()) {
                case MESSAGE -> chatFlow.readMessage();
                case INPUT -> chatFlow.enterInput(inputs[inputCounter++]);
                case CHOICE -> chatFlow.enterChoice(inputs[inputCounter++]);
                case END -> continueChat = false;
            }
        }

        HashMap<String, String> expectedValues = new HashMap<>();
        expectedValues.put("firstName", "Mārtiņš Miķelis");
        expectedValues.put("lastName", "Šķūriņš-Čužāns");
        expectedValues.put("gender", "male");

        assertEquals(expectedValues, chatFlow.getUserVariables());
    }

    @Test
    void regexShouldAcceptSpanishNames() {
        boolean continueChat = true;
        String[] inputs = {"José", "García Gómez", "male"};
        int inputCounter = 0;

        while (continueChat) {
            switch (chatFlow.getInstructionType()) {
                case MESSAGE -> chatFlow.readMessage();
                case INPUT -> chatFlow.enterInput(inputs[inputCounter++]);
                case CHOICE -> chatFlow.enterChoice(inputs[inputCounter++]);
                case END -> continueChat = false;
            }
        }

        HashMap<String, String> expectedValues = new HashMap<>();
        expectedValues.put("firstName", "José");
        expectedValues.put("lastName", "García Gómez");
        expectedValues.put("gender", "male");

        assertEquals(expectedValues, chatFlow.getUserVariables());
    }

    @Test
    void regexShouldAcceptJapaneseNames() {
        boolean continueChat = true;
        String[] inputs = {"駿", "宮崎", "male"};
        int inputCounter = 0;

        while (continueChat) {
            switch (chatFlow.getInstructionType()) {
                case MESSAGE -> chatFlow.readMessage();
                case INPUT -> chatFlow.enterInput(inputs[inputCounter++]);
                case CHOICE -> chatFlow.enterChoice(inputs[inputCounter++]);
                case END -> continueChat = false;
            }
        }

        HashMap<String, String> expectedValues = new HashMap<>();
        expectedValues.put("firstName", "駿");
        expectedValues.put("lastName", "宮崎");
        expectedValues.put("gender", "male");

        assertEquals(expectedValues, chatFlow.getUserVariables());
    }

    @Test
    void shouldInformAboutInvalidEntries() {
        boolean continueChat = true;
        String[] inputs = {"Ann", "Doe", "spoon","female"};
        int inputCounter = 0;

        while (continueChat) {
            switch (chatFlow.getInstructionType()) {
                case MESSAGE -> chatFlow.readMessage();
                case INPUT -> {
                    String errorMessage = chatFlow.getInstruction().getErrorMessage();

                    chatFlow.enterInput("$");
                    assertEquals(errorMessage, chatFlow.readMessage());

                    chatFlow.enterInput("{");
                    assertEquals(errorMessage, chatFlow.readMessage());

                    chatFlow.enterInput("}");
                    assertEquals(errorMessage, chatFlow.readMessage());

                    chatFlow.enterInput("&");
                    assertEquals(errorMessage, chatFlow.readMessage());

                    chatFlow.enterInput("#");
                    assertEquals(errorMessage, chatFlow.readMessage());

                    chatFlow.enterInput("*");
                    assertEquals(errorMessage, chatFlow.readMessage());

                    chatFlow.enterInput("***${}Johnny");
                    assertEquals(errorMessage, chatFlow.readMessage());

                    chatFlow.enterInput("^Smith%");
                    assertEquals(errorMessage, chatFlow.readMessage());

                    chatFlow.enterInput(inputs[inputCounter++]);
                }
                case CHOICE -> {
                    String errorMessage = chatFlow.getInstruction().getErrorMessage();

                    chatFlow.enterChoice("spoon");
                    assertEquals(errorMessage, chatFlow.readMessage());

                    chatFlow.enterChoice("!#---*5");
                    assertEquals(errorMessage, chatFlow.readMessage());

                    chatFlow.enterChoice(inputs[inputCounter++]);
                }
                case END -> continueChat = false;
            }
        }

        HashMap<String, String> expectedValues = new HashMap<>();
        expectedValues.put("firstName", "Ann");
        expectedValues.put("lastName", "Doe");
        expectedValues.put("gender", "female");

        assertEquals(expectedValues, chatFlow.getUserVariables());
    }

    @Test
    void choiceShouldChangeMessage() {
        boolean continueChat = true;
        String[] inputs = {"A", "B", "male"};
        int inputCounter = 0;

        while (continueChat) {
            switch (chatFlow.getInstructionType()) {
                case MESSAGE -> chatFlow.readMessage();
                case INPUT -> chatFlow.enterInput(inputs[inputCounter++]);
                case CHOICE -> {
                    chatFlow.enterChoice(inputs[inputCounter++]);
                    String reply = chatFlow.readMessage();
                    assertEquals("Mr.A", reply.split(" ")[1]);
                }
                case END -> continueChat = false;
            }
        }

        inputs[2] = "female";
        continueChat = true;
        while (continueChat) {
            switch (chatFlow.getInstructionType()) {
                case MESSAGE -> chatFlow.readMessage();
                case INPUT -> chatFlow.enterInput(inputs[inputCounter++]);
                case CHOICE -> {
                    chatFlow.enterChoice(inputs[inputCounter++]);
                    String reply = chatFlow.readMessage();
                    assertEquals("Mrs.A", reply.split(" ")[1]);
                }
                case END -> continueChat = false;
            }
        }
    }
}