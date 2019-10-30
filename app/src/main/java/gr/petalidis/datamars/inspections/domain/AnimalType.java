
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

package gr.petalidis.datamars.inspections.domain;

import gr.petalidis.datamars.Moo;
import gr.petalidis.datamars.R;

public enum AnimalType {
    KID_ANIMAL(Moo.getAppContext().getResources().getString(R.string.kidAnimal)),
    GOAT_ANIMAL(Moo.getAppContext().getResources().getString(R.string.goatAnimal)),
    LAMB_ANIMAL(Moo.getAppContext().getResources().getString(R.string.lambAnimal)),
    SHEEP_ANIMAL(Moo.getAppContext().getResources().getString(R.string.sheepAnimal)),
    HEGOAT_ANIMAL(Moo.getAppContext().getResources().getString(R.string.heGoatAnimal)),
    RAM_ANIMAL(Moo.getAppContext().getResources().getString(R.string.ramAnimal)),
    HORSE_ANIMAL(Moo.getAppContext().getResources().getString(R.string.horseAnimal)),
    KIDLAMB_ANIMAL("Αμνοερίφια");

    final String title;

    AnimalType(java.lang.String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
