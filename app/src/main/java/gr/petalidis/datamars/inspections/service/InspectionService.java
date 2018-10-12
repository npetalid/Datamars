package gr.petalidis.datamars.inspections.service;

import java.text.ParseException;
import java.util.List;

import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.dto.InspectionDateProducer;
import gr.petalidis.datamars.inspections.exceptions.PersistenceException;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.repository.EntryRepository;
import gr.petalidis.datamars.inspections.repository.InspectionRepository;

public class InspectionService {

    public static List<InspectionDateProducer> findAllInspections(DbHandler dbHandler) throws PersistenceException
    {
        try {
            return InspectionRepository.getAllInspections(dbHandler);
        } catch (ParseException e) {
            throw new PersistenceException(e.getLocalizedMessage());
        }
    }
    public static Inspection findInspectionFor(DbHandler dbHandler, String uuidString) throws PersistenceException
    {
        try {
            return InspectionRepository.getInspectionFor(dbHandler,uuidString);
        } catch (ParseException e) {
            throw new PersistenceException(e.getLocalizedMessage());
        }
    }

    public static Inspection save(DbHandler dbHandler,Inspection inspection)
    {
        InspectionRepository.save(dbHandler, inspection);
        EntryRepository.save(dbHandler,inspection.getValidEntries());
        return inspection;
    }
    public List<String> getCsvString(DbHandler dbHandler, String uuidString) throws PersistenceException{
        try {
            Inspection inspection =  InspectionRepository.getInspectionFor(dbHandler,uuidString);
            return inspection.toStrings();
        } catch (ParseException e) {
            throw new PersistenceException(e.getLocalizedMessage());
        }
    }
}
