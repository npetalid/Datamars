package gr.petalidis.datamars.inspections.service;

import java.text.ParseException;
import java.util.List;

import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.domain.ScannedDocument;
import gr.petalidis.datamars.inspections.dto.InspectionDateProducer;
import gr.petalidis.datamars.inspections.exceptions.PersistenceException;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.repository.EntryRepository;
import gr.petalidis.datamars.inspections.repository.InspectionRepository;
import gr.petalidis.datamars.inspections.repository.ScannedDocumentRepository;

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
        ScannedDocumentRepository.save(dbHandler,inspection.getScannedDocuments());
        return inspection;
    }

    public static Inspection updatePhotos(DbHandler dbHandler,Inspection inspection, List<ScannedDocument> scannedDocuments)
    {

        ScannedDocumentRepository.save(dbHandler,scannedDocuments);
        inspection.getScannedDocuments().addAll(scannedDocuments);
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
