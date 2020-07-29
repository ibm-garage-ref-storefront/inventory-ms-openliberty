package dev.appsody.starter.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Inventory", description = "POJO that represents the Inventory item")
public class Inventory {
    @Getter @Setter private long id;

    @Getter @Setter private String name;

    @Getter @Setter private String description;

    @Getter @Setter private int price;

    @Getter @Setter private String img_alt;

    @Getter @Setter private String img;

    @Getter @Setter private int stock;

    public Inventory() {
    }

    public Inventory(long id) {
        this.id = id;
    }

    public Inventory(String name, String description, int price, String img_alt, String img, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.img = img;
        this.img_alt = img_alt;
        this.stock = stock;
    }

    public String toString() {
        return "{\n" +
                String.format("\t\"id\": %s,\n", this.id) +
                String.format("\t\"name\": \"%s\",\n", this.name) +
                String.format("\t\"description\": \"%s\",\n", this.description) +
                String.format("\t\"price\": %s,\n", this.price) +
                String.format("\t\"imgAlt\": \"%s\",\n", this.img_alt) +
                String.format("\t\"img\": \"%s\",\n", this.img) +
                String.format("\t\"stock\": %s\n", this.stock) +
                "}";
    }
}
