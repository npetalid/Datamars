package gr.petalidis.datamars.inspections.service;

import org.apache.log4j.Logger;

import java.util.List;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.inspections.domain.UsbAlias;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.repository.UsbAliasRepository;

public class UsbAliasService {

    private static final Logger log = Log4jHelper.getLogger(InspectionService.class.getName());

    public static List<UsbAlias> findAndUpdateAllUsbs(DbHandler dbHandler, List<String> currentlyFoundUsbs)
    {
        List<UsbAlias> alreadyPresentUsbToAlias = UsbAliasRepository.getUsbAliases(dbHandler);
        if (currentlyFoundUsbs!=null) {
            currentlyFoundUsbs.forEach(present -> {
                UsbAlias usbAlias = new UsbAlias(present, present);
                if (!alreadyPresentUsbToAlias.contains(usbAlias)) {
                    UsbAliasRepository.save(dbHandler, usbAlias);
                    alreadyPresentUsbToAlias.add(usbAlias);
                }
            });
        }
        return alreadyPresentUsbToAlias;
    }
    public static void updateWithNewAlias(DbHandler dbHandler, UsbAlias usbAlias) {
        UsbAliasRepository.saveOrUpdate(dbHandler,usbAlias);
    }
}
