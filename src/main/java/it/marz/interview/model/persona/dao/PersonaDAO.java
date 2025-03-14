package it.marz.interview.model.persona.dao;

import it.marz.interview.exception.ItemAlreadyExistsException;
import it.marz.interview.exception.ItemNotFoundException;
import it.marz.interview.model.persona.Persona;

import java.util.List;

public interface PersonaDAO {

        public String insertPersona(Persona persona) throws ItemAlreadyExistsException;
        public String editPersona(Persona persona);
        public Persona getPersonaById(int id) throws ItemNotFoundException;
        public List<Persona> getAllPersona();
        public void removePersona(Persona persona);
}
