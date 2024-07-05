package lv.raitiss;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringSubstitutor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatFlow {
    private List<ChatInstruction> chatInstructions;
    private HashMap<String, String> userVariables = new HashMap<>();
    private ChatInstruction instruction;

    public ChatFlow(List<ChatInstruction> chatInstructions) {
        this.chatInstructions = chatInstructions;
        verifyInstructions(chatInstructions);
        this.instruction = chatInstructions.getFirst();
    }

    public ChatFlow(String pathToInstructionsJSON) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<ChatInstruction>> instructionList = new TypeReference<List<ChatInstruction>>() {
        };
        this.chatInstructions = objectMapper.readValue(new File(pathToInstructionsJSON), instructionList);
        verifyInstructions(chatInstructions);
        this.instruction = chatInstructions.getFirst();
    }

    public String readMessage() {
        String message = instruction.getMessage();
        instruction = findInstructionById(instruction.getNextId());
        return StringSubstitutor.replace(message, userVariables, "${", "}");
    }

    public void enterInput(String input) {
        if (instruction.getType().equals(InstructionType.INPUT)) {
            if (input.matches(instruction.getInputRegex())) {
                userVariables.put(instruction.getInputVariable(), input);
                instruction = findInstructionById(instruction.getNextId());
            } else {
                instruction = showErrorMessage();
            }
        }
    }

    public void enterChoice(String choice) {
        if (instruction.getType().equals(InstructionType.CHOICE)) {
            HashMap<String, Integer> choices = instruction.getChoices();
            if (choices.containsKey(choice)) {
                this.userVariables.put(instruction.getInputVariable(), choice);
                instruction = findInstructionById(choices.get(choice));
            } else {
                instruction = showErrorMessage();
            }
        }
    }

    private void verifyInstructions(List<ChatInstruction> instructions) {
        StringBuilder errors = new StringBuilder();
        Pattern placeholderPattern = Pattern.compile("(\\$\\{)(\\w+)(})");
        List<String> inputVariables = new ArrayList<>();
        List<String> messageVariables = new ArrayList<>();
        boolean hasEnd = false;

        for (int i = 0; i < instructions.size(); i++) {
            ChatInstruction chatInstruction = chatInstructions.get(i);
            String missingValues = chatInstruction.findMissingValues();

            if (missingValues != null) {
                errors.append("\nInstruction #").append(i).append(": ").append(missingValues);
            }

            switch (chatInstruction.getType()) {
                case null -> {}
                case INPUT, CHOICE -> inputVariables.add(chatInstruction.getInputVariable());
                case MESSAGE -> {
                    if(chatInstruction.getMessage() != null){
                        Matcher matcher = placeholderPattern.matcher(chatInstruction.getMessage());
                        while (matcher.find()) {
                            messageVariables.add(matcher.group(2));
                        }
                    }
                }
                case END -> hasEnd = true;
            }
        }

        for (String messageVariable : messageVariables) {
            if (!inputVariables.contains(messageVariable)) {
                errors.append("\nVariable \"").append(messageVariable).append("\" has no input.");
            }
        }

        if (!hasEnd) {
            errors.append("Instructions don't have a defined end.");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.toString());
        }
    }

    private ChatInstruction findInstructionById(int id) {
        return chatInstructions.stream()
                .filter(instr -> instr.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private ChatInstruction showErrorMessage() {
        ChatInstruction errorMessage = new ChatInstruction();
        errorMessage.setType(InstructionType.MESSAGE);
        errorMessage.setMessage(instruction.getErrorMessage());
        errorMessage.setNextId(instruction.getId());
        return errorMessage;
    }

    public HashMap<String, String> getUserVariables() {
        return userVariables;
    }

    public InstructionType getInstructionType() {
        return instruction.getType();
    }

    public ChatInstruction getInstruction() {
        return this.instruction;
    }
}
