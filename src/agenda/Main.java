package agenda;

import java.io.IOException;

public class Main {

    public static void main(String... args) {
        Terminal terminal = new Terminal();

        while (terminal.isOpen()) {
            try {
                terminal.nextLink();
            } catch (InvalidLinkExceptionFactory.InvalidLinkException e) {
                System.err.println(e.getMessage());
            } catch (IOException ee) {
                System.exit(1);
            }
        }

        System.exit(0);
    }

}
