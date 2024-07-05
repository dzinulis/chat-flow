package lv.raitiss;

import java.util.HashMap;

public class ChatInstruction {
    private Integer id;
    private InstructionType type;
    private String message;
    private String inputVariable;
    private String inputRegex;
    private HashMap<String, Integer> choices;
    private String errorMessage;
    private Integer nextId;

    public String findMissingValues() {
        StringBuilder errors = new StringBuilder();

        if (this.id == null) errors.append("\nMissing id;");

        switch (this.type) {
            case MESSAGE -> {
                if (this.message == null) errors.append("\nMissing message;");
                if (this.nextId == null) errors.append("\nMissing next instruction id;");
            }
            case INPUT -> {
                if (this.inputVariable == null) errors.append("\nMissing an input variable.");
                if (this.errorMessage == null) errors.append("\nMissing an error message.");
                if (this.nextId == null) errors.append("\nMissing next instruction id.");
            }
            case CHOICE -> {
                if (this.inputVariable == null) errors.append("\nMissing an input variable.");
                if (this.errorMessage == null) errors.append("\nMissing an error message.");
                if (this.choices == null) errors.append("\nMissing choice list.");
                else if (this.choices.isEmpty()) errors.append("\nMissing choice list.");
            }
            case END -> {
            }
            case null -> errors.append("\nMissing a type");
        }

        if (errors.isEmpty()) return null;
        else return errors.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InstructionType getType() {
        return type;
    }

    public void setType(InstructionType type) {
        this.type = type;
    }

    public String getInputVariable() {
        return inputVariable;
    }

    public void setInputVariable(String inputVariable) {
        this.inputVariable = inputVariable;
    }

    public HashMap<String, Integer> getChoices() {
        return choices;
    }

    public void setChoices(HashMap<String, Integer> choices) {
        this.choices = choices;
    }

    public Integer getNextId() {
        return nextId;
    }

    public void setNextId(Integer nextId) {
        this.nextId = nextId;
    }

    public String getInputRegex() {
        return inputRegex;
    }

    public void setInputRegex(String inputRegex) {
        this.inputRegex = inputRegex;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
