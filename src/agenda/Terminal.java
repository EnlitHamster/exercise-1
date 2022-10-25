package agenda;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Pattern;

import agenda.InvalidLinkExceptionFactory.InvalidLinkException;

/**
 * Lanciata quando viene interrogato per input un terminal chiuso.
 */
class ClosedTerminalException extends IOException {
    ClosedTerminalException() {
        super("Terminal was closed. Cannot produce output.");
    }
}

class InvalidLinkExceptionFactory {

    /**
     * Rappresenta lo stato di salute di un link, ossia se funziona, se è invalido
     * o se non è raggiungibile.
     */
    static enum LinkHealth {
        OK,
        INVALID,
        UNREACHABLE,
    }

    /**
     * Lanciata quando viene inserito un link invalido. Può essere lanciata solo
     * attraverso <code>InvalidLinkExceptionFactory::create</code>.
     */
    static class InvalidLinkException extends IOException {
        protected InvalidLinkException(String message) {
            super(message);
        }
    }

    boolean alreadyExists;
    LinkHealth linkHealth;

    boolean isValid() {
        return !this.alreadyExists && linkHealth == LinkHealth.OK;
    }

    private String getIssues() {
        ArrayList<String> messageStack = new ArrayList<String>();
        if (this.alreadyExists) {
            messageStack.add("already exists");
        }
        switch (this.linkHealth) {
            case INVALID:
                messageStack.add("is invalid");
                break;
            case UNREACHABLE:
                messageStack.add("does not work");
                break;
            default:
                break;
        }
        return String.join(" and ", messageStack);
    }

    InvalidLinkException create() {
        return new InvalidLinkException(
                new StringBuilder()
                        .append("The link has not been added because it ")
                        .append(this.getIssues())
                        .toString());
    }
}

/**
 * Rappresenta un terminale per l'utente per connettersi alla banca dati di
 * link.
 */
public class Terminal {
    private CLI cli;
    private boolean open;

    /**
     * Specifica quali sono i protocolli (http, https per ora) accettati.
     */
    private static final Pattern VALID_LINK = Pattern.compile("^http(s)?://");

    Terminal() {
        this.cli = new CLI();
        this.open = true;
        System.out.println("Terminale aperto.");
    }

    boolean isOpen() {
        return this.open;
    }

    /**
     * Termina l'accesso dell'utente alla banca dati.
     */
    private void close() {
        this.open = false;
        this.saveLinks();
        System.out.println("Terminale terminato.");
    }

    /**
     * Salva i link su file.
     */
    private void saveLinks() {
        // TODO: Implementare
    }

    /**
     * Vedi <code>VALID_PROTOCOLS</code> per i rotocolli accettati.
     * 
     * @param link il link
     * @return se il link è valido.
     */
    private boolean isLinkValid(String link) {
        return VALID_LINK.matcher(link).find();
    }

    /**
     * Tenta di connettersi al link per confermare che sia funzionante.
     * 
     * @param link il link
     * @return se il link funziona.
     */
    private boolean isLinkWorking(String link) {
        try {
            URLConnection connection = new URL(link).openConnection();
            if (connection instanceof HttpURLConnection) {
                return ((HttpURLConnection) connection).getResponseCode() == HttpURLConnection.HTTP_OK;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Richiede all'utente un nuovo link.
     */
    void nextLink() throws InvalidLinkException, ClosedTerminalException, IOException {
        if (!this.open) {
            throw new ClosedTerminalException();
        }

        try {
            String link = this.cli.askForString().trim();
            if (link.isEmpty()) {
                this.close();
            }

            this.testLink();
        } catch (IOException e) {
            this.close();
            throw e;
        }
    }

    private void testLink() {
        // TODO: Implementare
    }
}
