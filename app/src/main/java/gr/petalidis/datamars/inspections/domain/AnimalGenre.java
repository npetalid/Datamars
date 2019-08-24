package gr.petalidis.datamars.inspections.domain;

enum AnimalGenre {
    NONE("Εκτός βιολογικής"),
    BIO("Βιολογικής");

    private String name;

    AnimalGenre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
