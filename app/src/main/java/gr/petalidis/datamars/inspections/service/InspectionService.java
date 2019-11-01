/*
 * Copyright 2017-2019 Nikolaos Petalidis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package gr.petalidis.datamars.inspections.service;

import org.slf4j.Logger;

import java.text.ParseException;
import java.util.List;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.domain.ScannedDocument;
import gr.petalidis.datamars.inspections.dto.InspectionDateProducer;
import gr.petalidis.datamars.inspections.exceptions.PersistenceException;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.repository.EntryRepository;
import gr.petalidis.datamars.inspections.repository.InspectionRepository;
import gr.petalidis.datamars.inspections.repository.OtherEntryRepository;
import gr.petalidis.datamars.inspections.repository.ScannedDocumentRepository;

public class InspectionService {
    private static final Logger log = Log4jHelper.getLogger(InspectionService.class.getName());

    public static List<InspectionDateProducer> findAllInspections(DbHandler dbHandler) throws PersistenceException
    {
        try {
            return InspectionRepository.getAllInspections(dbHandler);
        } catch (Exception e) {
            log.error("findAllInspections(): Unable to get inspection: {}", e.getLocalizedMessage());
            throw new PersistenceException(e.getLocalizedMessage());
        }
    }
    public static Inspection findInspectionFor(DbHandler dbHandler, String uuidString) throws PersistenceException
    {
        try {
            return InspectionRepository.getInspectionFor(dbHandler,uuidString);
        } catch (ParseException e) {
            log.error("findInspectionFor(): Unable to get inspection: {}", e.getLocalizedMessage());
            throw new PersistenceException(e.getLocalizedMessage());
        }
    }

    public static Inspection save(DbHandler dbHandler,Inspection inspection)
    {
        InspectionRepository.save(dbHandler, inspection);
        EntryRepository.save(dbHandler,inspection.getValidEntries());
        OtherEntryRepository.save(dbHandler,inspection.getConventionalTags());
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
            log.error("getCsvString(): Unable to get inspection: {}", e.getLocalizedMessage());
            throw new PersistenceException(e.getLocalizedMessage());
        }
    }
}
