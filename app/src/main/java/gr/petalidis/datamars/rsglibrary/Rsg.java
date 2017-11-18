package gr.petalidis.datamars.rsglibrary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by npetalid on 24/10/17. ISO Long (default): - Animal:
 * "A0000000964000000123456" (23 char) A 0 00 000 0964 000000123456 1st char: A
 * = Animal 2nd char: 0 = Retagging from 0 to 7 3rd and 4th chars: 00 = User
 * information 5th to 7th chars: 000 = Additional information field (unused) 8th
 * to 11th chars: 0964 = Country code 12th to 23rd chars: 000000123456 =
 * Identification code
 */
public class Rsg {

    private String identificationCode;
    private String countryCode;

    private Date date;

    public Rsg(String identificationCode, String date, String time) throws ParseException {

        if (identificationCode.length() != 23) {
            throw new ParseException("We currently read only Long ISO EIC format", 0);
        }

        this.identificationCode = identificationCode.substring(11, 23);
        this.countryCode = identificationCode.substring(7, 11);

        SimpleDateFormat format =  new SimpleDateFormat("ddMMyyyy HHmmss", Locale.US);
        try {
            this.date = format.parse(date + " " + time);
        } catch (ParseException e) {
            //Retry with different time format
            format = new SimpleDateFormat("ddMMyyyy Hmmss", Locale.US);
            this.date = format.parse(date + " " + time);
        }
    }

    public Rsg(String countryCode, String identificationCode, Date date)
    {
        this.countryCode = countryCode;
        this.identificationCode = identificationCode;
        this.date = date;
    }

    public String getIdentificationCode() {
        return identificationCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
        return format.format(date);
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hms");

        return countryCode + "," + identificationCode + "," + format.format(date);
    }

    @Override
    public int hashCode() {
        return 4 * (this.identificationCode == null ? 0 : this.identificationCode.hashCode())
                + 2 * (this.countryCode == null ? 0 : this.countryCode.hashCode())
                + (this.date == null ? 0 : this.date.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rsg other = (Rsg) obj;

        if (!this.identificationCode.equals(other.identificationCode)) {
            return false;
        }
        if (!this.countryCode.equals(other.countryCode)) {
            return false;
        }
        
        return this.date.equals(other.date);
    }

}
