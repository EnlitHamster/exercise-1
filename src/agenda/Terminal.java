package agenda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;
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
    private final ArrayList<String> database = new ArrayList<>();

    private static final String DATABASE_NAME = "links.db";
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
        try(PrintWriter writer = new PrintWriter(DATABASE_NAME)){
            for(String link : database) {
                if(!Objects.equals(link, database.get(database.size() - 1))) writer.print(link + ",");
                else writer.print(link);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Vedi <code>VALID_PROTOCOLS</code> per i protocolli accettati.
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
                return; //Per non proseguire il codice e non lanciare l'eccezione invalidlinkexception (poco convincente *.*)
            }
            this.testLink(link);

        //aggiunto questo catch sennò si finisce sempre nell'eccezione generica I/O
        //e si interrompeva il loop poichè viene chiamato this.close()
        //non è questo che si vuole
        }catch(InvalidLinkExceptionFactory.InvalidLinkException e){
         throw e;
        }catch (IOException e) {
                this.close();
                throw e;
        }
    }

    /**
     * Verifica il link se è valido e raggiungibile
     * @param link il link
     * @throws InvalidLinkException se il link è INVALID o UNREACHABLE
     */
    private void testLink(String link) throws InvalidLinkException {
        //if(link == null) throw new IOException("Link must not be null");

        InvalidLinkExceptionFactory invalidLink = new InvalidLinkExceptionFactory();


        if(!this.isLinkValid(link)){
            invalidLink.linkHealth = InvalidLinkExceptionFactory.LinkHealth.INVALID;
            throw invalidLink.create();
        }
        if(!this.isLinkWorking(link)){
            invalidLink.linkHealth = InvalidLinkExceptionFactory.LinkHealth.UNREACHABLE;
            throw invalidLink.create();
        }
        invalidLink.linkHealth = InvalidLinkExceptionFactory.LinkHealth.OK;

        if(database.contains(link)){
            invalidLink.alreadyExists = true;
            throw invalidLink.create();
        }
        if(invalidLink.isValid()){
            this.database.add(link);
        }
    }
}
