package gr.petalidis.datamars.inspections.domain;

enum AnimalGenre {
    NONE("Όχι σπάνια"),
    BIO("Βιολογικής");

    private final String name;

    AnimalGenre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
