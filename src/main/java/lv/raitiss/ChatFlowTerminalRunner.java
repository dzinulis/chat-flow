package lv.raitiss;

import java.util.Scanner;

public class ChatFlowTerminalRunner {
    private ChatFlow chatFlow;

    public ChatFlowTerminalRunner(ChatFlow chatFlow) {
        this.chatFlow = chatFlow;
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        boolean continueChat = true;

        while (continueChat) {
            switch (chatFlow.getInstructionType()) {
                case MESSAGE -> System.out.println(chatFlow.readMessage());
                case INPUT -> chatFlow.enterInput(in.nextLine());
                case CHOICE -> chatFlow.enterChoice(in.nextLine());
                case END -> continueChat = false;
            }
        }
    }
}
