package gr.petalidis.datamars.inspections.domain;

import gr.petalidis.datamars.Moo;
import gr.petalidis.datamars.R;

public interface Animals {
    String KID_ANIMAL = Moo.getAppContext().getResources().getString(R.string.kidAnimal);
    String GOAT_ANIMAL = Moo.getAppContext().getResources().getString(R.string.goatAnimal);
    String LAMB_ANIMAL = Moo.getAppContext().getResources().getString(R.string.lambAnimal);
    String SHEEP_ANIMAL = Moo.getAppContext().getResources().getString(R.string.sheepAnimal);
    String HEGOAT_ANIMAL = Moo.getAppContext().getResources().getString(R.string.heGoatAnimal);
    String RAM_ANIMAL = Moo.getAppContext().getResources().getString(R.string.ramAnimal);
    String HORSE_ANIMAL = Moo.getAppContext().getResources().getString(R.string.horseAnimal);
}
