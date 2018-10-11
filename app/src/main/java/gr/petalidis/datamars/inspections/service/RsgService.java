package gr.petalidis.datamars.inspections.service;

import java.util.Date;
import java.util.Set;

import gr.petalidis.datamars.rsglibrary.Rsg;

public interface RsgService {

    public Set<Date> getInspectionDates();
    public Set<Rsg> getRsgForDate(Date date);
}
