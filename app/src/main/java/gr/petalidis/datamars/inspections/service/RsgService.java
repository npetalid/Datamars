
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

import java.util.Date;
import java.util.Set;

import gr.petalidis.datamars.rsglibrary.Rsg;

interface RsgService {

    Set<Date> getInspectionDates();
    Set<Rsg> getRsgForDate(Date date);
}
