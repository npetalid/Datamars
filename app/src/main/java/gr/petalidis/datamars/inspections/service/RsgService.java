package gr.petalidis.datamars.inspections.service;

import java.util.Date;
import java.util.Set;

import gr.petalidis.datamars.rsglibrary.Rsg;

interface RsgService {

    Set<Date> getInspectionDates();
    Set<Rsg> getRsgForDate(Date date);
}
