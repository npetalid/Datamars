package gr.petalidis.datamars.inspections.dto;

import java.io.Serializable;

public class ThumbnailDto implements Serializable {
    private final String imagePath;
   private String name;

    public ThumbnailDto( String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }


    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

}
