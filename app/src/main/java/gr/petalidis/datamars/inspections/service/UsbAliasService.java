
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

import java.util.List;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.inspections.domain.UsbAlias;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.repository.UsbAliasRepository;

public class UsbAliasService {

    private static final Logger log = Log4jHelper.getLogger(UsbAliasService.class.getName());

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
