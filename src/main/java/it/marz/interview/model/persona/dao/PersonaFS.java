package it.marz.interview.model.persona.dao;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import it.marz.interview.exception.ItemAlreadyExistsException;
import it.marz.interview.exception.ItemNotFoundException;
import it.marz.interview.model.persona.Persona;
import it.marz.interview.utils.CSVManager;
import it.marz.interview.utils.ConstantMsg;
import it.marz.interview.utils.LoggerManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaFS implements PersonaDAO{
    private static final String CSV_FILE_NAME;

    static {
        try {
            CSV_FILE_NAME = CSVManager.getCsvDir() + "informazioni.txt";
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private final File file;
    private static final int INDEX_ID = 0;
    private static final int INDEX_NOME = 1;
    private static final int INDEX_COGNOME = 2;
    private static final int INDEX_INDIRIZZO = 3;
    private static final int INDEX_TELEFONO = 4;
    private static final int INDEX_ETA = 5;

    public PersonaFS() throws IOException {
        this.file = new File(CSV_FILE_NAME);

        if(!file.exists()) {
            boolean isFileCreated = file.createNewFile();
            if(!isFileCreated) throw new IOException("Impossibile dialogare con il file");
        }
    }
    @Override
    public String insertPersona(Persona persona) throws ItemAlreadyExistsException {
        boolean duplicatedRecordId;
        Persona p = null;
        String[] rcrd;
        CSVWriter csvWriter = null;
        try {
            p = getPersonaById(persona.getId());
            duplicatedRecordId = p != null;
        } catch (ItemNotFoundException e) {
            duplicatedRecordId = false;
        }

        if(duplicatedRecordId) throw new ItemAlreadyExistsException("Persona esistente! Due persone non possono avere lo stesso numero di telefono!");

        try {
            csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, true)),  ICSVWriter.DEFAULT_SEPARATOR,
                    ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.RFC4180_LINE_END);

            List<Persona> personaList = getAllPersona();
            int lastId = 0;
            if(!personaList.isEmpty())
                lastId = personaList.getLast().getId();
            persona.setId(lastId + 1);

            rcrd = setRecordFromPersona(persona);

            csvWriter.writeNext(rcrd);
            csvWriter.flush();

        } catch (IOException e) {
            LoggerManager.logSevereException("Impossibile scrivere file!", e);
            return "Errore in inserimento";

        }finally {
            CSVManager.closeCsvWriter(csvWriter);
        }

        return "Inserimento effettuato correttamente!";
    }

    @Override
    public String editPersona(Persona persona) {
        boolean isExistingPerson = false;
        CSVReader csvReader = null;
        CSVWriter csvWriter = null;
        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd;

            List<String[]> updatedRecords = new ArrayList<>();

            while ((rcrd = csvReader.readNext()) != null) {
                //check if the user exists
                if(rcrd[INDEX_ID].equals(String.valueOf(persona.getId()))) {
                    isExistingPerson = true;
                    rcrd = setRecordFromPersona(persona);
                }
                updatedRecords.add(rcrd);
            }

            if(isExistingPerson) {
                csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, false)),  ICSVWriter.DEFAULT_SEPARATOR,
                        ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.RFC4180_LINE_END);
                csvWriter.writeAll(updatedRecords);
                csvWriter.flush();
                return "Modifica effettuata!";
            } else
                return "Utente non trovato.";

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            return "Modifica impossibile";
        } finally {
            CSVManager.closeCsvReader(csvReader);
            CSVManager.closeCsvWriter(csvWriter);
        }
    }

    @Override
    public Persona getPersonaById(int id) throws ItemNotFoundException {
        Persona p = null;
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd = {};

            while ((rcrd = csvReader.readNext()) != null) {
                //check if the user exists
                if(rcrd[INDEX_ID].equals(String.valueOf(id))) {
                    p = setPersonaFromRecord(rcrd);
                }
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            throw new ItemNotFoundException("Persona non esistente.");
        } finally {
            CSVManager.closeCsvReader(csvReader);
        }

        if(p == null) throw new ItemNotFoundException("Persona non esistente");

        return p;
    }

    @Override
    public List<Persona> getAllPersona() {
        List<Persona> personaList = new ArrayList<>();
        CSVReader csvReader = null;

        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd = {};

            while ((rcrd = csvReader.readNext()) != null) {
                Persona p = setPersonaFromRecord(rcrd);
                personaList.add(p);
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            return personaList;
        } finally {
            CSVManager.closeCsvReader(csvReader);
        }

        return personaList;
    }

    @Override
    public void removePersona(Persona persona) {
        boolean isExistingPerson = false;
        CSVReader csvReader = null;
        CSVWriter csvWriter = null;
        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd;

            List<String[]> updatedRecords = new ArrayList<>();

            while ((rcrd = csvReader.readNext()) != null) {
                //check if the user exists
                if(!rcrd[INDEX_ID].equals(String.valueOf(persona.getId()))) {
                    updatedRecords.add(rcrd);
                } else {
                    isExistingPerson = true;
                    //break;
                }
            }

            if(isExistingPerson) {
                csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, false)),  ICSVWriter.DEFAULT_SEPARATOR,
                        ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.RFC4180_LINE_END);
                csvWriter.writeAll(updatedRecords);
                csvWriter.flush();
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
        } finally {
            CSVManager.closeCsvReader(csvReader);
            CSVManager.closeCsvWriter(csvWriter);
        }
    }

    private String[] setRecordFromPersona(Persona p) {
        String[] rcrd = new String[6];

        rcrd[INDEX_ID] = String.valueOf(p.getId());
        rcrd[INDEX_NOME] = p.getNome();
        rcrd[INDEX_COGNOME] = p.getCognome();
        rcrd[INDEX_INDIRIZZO] = p.getIndirizzo();
        rcrd[INDEX_TELEFONO] = p.getTelefono();
        rcrd[INDEX_ETA] = String.valueOf(p.getEta());
        return rcrd;
    }

    private Persona setPersonaFromRecord(String[] rcrd) {
        int id = Integer.parseInt(rcrd[INDEX_ID]);
        String nome = rcrd[INDEX_NOME];
        String cognome = rcrd[INDEX_COGNOME];
        String indirizzo = rcrd[INDEX_INDIRIZZO];
        String telefono = rcrd[INDEX_TELEFONO];
        int eta = Integer.parseInt(rcrd[INDEX_ETA]);

        return new Persona(id, nome, cognome, indirizzo, telefono, eta);
    }
}