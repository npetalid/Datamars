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
    HORSE_ANIMAL(Moo.getAppContext().getResources().getString(R.string.horseAnimal));

    final String title;

    AnimalType(java.lang.String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
