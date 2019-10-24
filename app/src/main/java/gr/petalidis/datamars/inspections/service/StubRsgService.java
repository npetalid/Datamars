package gr.petalidis.datamars.inspections.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import gr.petalidis.datamars.rsglibrary.Rsg;


public class StubRsgService implements RsgService {
    private final Set<Rsg> rsgs = new HashSet<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public StubRsgService() {
        List<Calendar> calendars = new ArrayList<>();

        for (int i=1;i<30;i++) {
            calendars.add(new GregorianCalendar(2018, Calendar.JULY, i));
        }
        calendars.forEach(x-> {
                    for (int i = 0; i < 40; i++) {
                        rsgs.add(new Rsg(getSaltString(), "0030", x.getTime()));
                    }
                }
        );
    }

    private String getSaltString() {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }


    @Override
    public Set<Date> getInspectionDates() {
        return rsgs.stream().map(rsg -> rsg.getDate()).collect(Collectors.toSet());
    }

    @Override
    public Set<Rsg> getRsgForDate(Date date) {
        return rsgs.stream().filter(x->dateFormat.format(x.getDate()).equals(dateFormat.format(date))).collect(Collectors.toSet());
    }
}
