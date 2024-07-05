package lv.raitiss;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        ChatFlow chatFlow = new ChatFlow("./src/main/resources/instructions_example.json");
        ChatFlowTerminalRunner terminalRunner = new ChatFlowTerminalRunner(chatFlow);
        terminalRunner.run();
        System.out.println(chatFlow.getUserVariables());
    }
}