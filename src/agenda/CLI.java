package agenda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Incapsula l'input da tastiera, ottimizzando l'IO.
 */
public class CLI {

    private BufferedReader buffer;

    CLI() {
        // NOTA: ogni input esterno a Java è uno stream, quindi va letto r,
        // poiché la console è un input con buffer (non ti produce input finché
        // l'utente non preme Enter), dobbiamo bufferizzarlo anche in Java.
        this.buffer = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Richiede all'utente una stringa. Se un messaggio è passato, questo viene
     * stampato a schermo prima dell'input, altrimenti viene mostrato un
     * carattere `> ` per informare l'utente che è atteso un input.
     * 
     * @param message -> messaggio per l'utente
     * @return l'input dell'utente
     * @throws IOException
     */
    public String askForString(String message) throws IOException {
        System.out.print(message);
        return this.buffer.readLine();
    }

    /**
     * Richiede all'utente una stringa. Se un messaggio è passato, questo viene
     * stampato a schermo prima dell'input, altrimenti viene mostrato un
     * carattere `> ` per informare l'utente che è atteso un input.
     * 
     * @return l'input dell'utente
     * @throws IOException
     */
    public String askForString() throws IOException {
        return this.askForString("> ");
    }
}
