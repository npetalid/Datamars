
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

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class OtherEntry implements Serializable {
    private UUID id = UUID.randomUUID();

    private UUID inspectionId;

    private OtherEntryType entryType = OtherEntryType.CONVENTIONAL;

    private Inspectee inspectee = new Inspectee("","");
    private String animal = "";
    private int count;

    public OtherEntry() {
    }

    public OtherEntry(Inspectee producer, String animal, int count, OtherEntryType type, UUID inspectionId) {
        this.inspectee = producer;
        this.animal = animal;
        this.count = count;
        this.inspectionId = inspectionId;
        this.entryType = type;
    }

    public Inspectee getInspectee() {
        return inspectee;
    }

    public void setInspectee(Inspectee inspectee) {
        this.inspectee = inspectee;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(UUID inspectionId) {
        this.inspectionId = inspectionId;
    }

    public OtherEntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(OtherEntryType entryType) {
        this.entryType = entryType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OtherEntry that = (OtherEntry) o;
        return inspectee.equals(that.inspectee) &&
                animal.equals(that.animal) &&
                entryType.equals(that.entryType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inspectee, animal, entryType);
    }
}
