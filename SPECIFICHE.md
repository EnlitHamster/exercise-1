Gestore di Segnalibri digitali
==============================

Durante le operazioni del team di provvisionamento (*Team P*), ci si è resi
conto di un grande problema nella catalogazione e condivisione delle persone ed
aziende d'interesse per il team di vendite (*Team V*).

> *Il precedente paragrafo si limita a presentare il problema come si è*
> *presentato nel mondo reale, e ci dà una chiara idea di cosa porti valore*
> *agli utenti del nostro prodotto: archiviare in un formato comparabile*
> *delle informazioni, un database insomma*

Poiché questi vengono condivisi come link in uno spreadsheet, spesso accade che
lo stesso link viene ripetuto, in quanto trovato da più membri del *Team P*,
causando dunque frustrazioni e rallentamenti al *Team V*.

> *Del background, questo è il paragrafo più ricco di informazioni, in quanto*
> *spiega il problema tecnico da risolvere*

La situazione si è complicata quando, alla prima proposta di semplicemente usare
un'estensione di Chrome creata per tale ragione da un'altra azienda, si è
scoperto che i membri del *Team P* utilizzano browser diversi, sistemi operativi
diversi e hanno metodologie di lavoro disparate.

# Problema

I nostri colleghi del *Team P* necessitano di un programma a linea di comando,
la cui funzionalità dopo l'avvio permette all'utente di scrivere o incollare
link, che vanno aggiunti al database. Premendo invio, questo viene aggiunto
al database se e solo se:

- [ ] lo stesso link non esiste nella base di dati.
- [ ] Il link è valido.
- [ ] Il link funziona.

Qualora anche solo una di queste condizioni non fosse soddisfatta, il programma
deve informare l'utente quale è stato il problema.

L'utente, a prescindere che il precedente inserimento sia andato a buon fine,
deve poter incollare nuovamente un altro link e proseguire con l'inserimento.

L'utente deve anche essere informato ad ogni momento di quali link ha già
inserito.

> *Questi ultimi paragrafi ci informano di quali sono le aspettative che gli*
> *utenti si aspettano da noi, possiamo definirli criteri di accettazione.*
> *Se un software non li rispetta, non è una soluzione.*

# Soluzione

La linea di comando presentata all'utente sarà composta da tre sezioni distinte

1. La parte più alta della linea di comando contiene tutti link già inseriti;
2. la penultima riga riporta all'utente potenziali errori relativi la sua ultima
   inserzione;
3. mentre l'ultima riga presenta lo spazio per l'utente per inserire link.

Poiché la linea di comando permette già di contenere uno storico delle ultime
operazioni, non è necessario preoccuparsi di implementarlo. Insomma, la linea di
comando ci risolve il punto 1. Questo ci dà anche il vantaggio che avremo anche
uno storico degli errori.

L'implementazione dei punti 2 e 3 è un semplice ciclo input, se l'utente non
inserisce niente e preme invio vuol dire che vuole terminare l'esecuzione.

> *I dettagli relativi* al ciclo *sono disponibili in*
> [`class Terminal`](./src/agenda/Terminal.java)

Al termine dell'esecuzione, i link devono essere salvati su un file, separati
con una virgola.

> *L'implementazione del ciclo principale del programma si trova nella classe*
> *`Terminal`, nel metodo `testLink`. Qui bisogna controllare il link e, se*
> *superati i test, aggiungerlo al database*

> *L'implementazione del salvataggio si trova nella classe `Terminal`, nel*
> *metodo `saveLinks`. Qui bisogna salvare i link su un file `links.db`*

# Modi d'uso

> *Per testare l'app avviare cone `java -jar ManagerSegnalibri.java`*

1. Inserire il link `https://www.fercam.com/it/contatti/contattaci-507.html`
2. Inserire il link `https://www.aitworldwide.com/contact-us`
3. Inserire il link `ftp://files.azienda3.com/users/staff/kruitz_valter.htm`
   - Link risulta invalido
4. Inserire il link `https://logisticsra.it/contattaci`
   - Link risulta non raggiungibile
5. Inserire il link `https://www.fercam.com/it/contatti/contattaci-507.html`
   - Link risulta già inserito
6. Termina gli inserimenti
7. Controlla che `links.db` contenga:
```
https://www.fercam.com/it/contatti/contattaci-507.html,https://www.aitworldwide.com/contact-us
```

# Note tecniche

Su Windows usare `make.bat`, su Linux usare `make`, e uno dei seguenti comandi
- `clean` per ripulire dai file di generazione;
- `setup` per generare le cartelle e i file necessari per generare l'eseguibile;
- `build` per eseguire `setup` e poi generare il file eseguibile;
- `build_clean` per eseguire `clean` e poi `build`.

Per eseguire l'app usare `java -jar ManagerSegnalibri.jar`.
