package gr.petalidis.datamars.inspections.domain;

enum AnimalGenre {
    NONE("Όχι σπάνια"),
    BIO("Βιολογικής");

    private String name;

    AnimalGenre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
