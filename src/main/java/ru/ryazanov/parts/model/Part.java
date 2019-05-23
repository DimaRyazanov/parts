package ru.ryazanov.parts.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "PARTS")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "Name")
    @Size(min = 3)
    @NotBlank
    private String name;

    @Column(name = "Count")
    @Min(0)
    private int count;

    @Column(name = "Required")
    private boolean isRequired;

    public Part() {
    }

    public Part(String name, int count, boolean isRequired) {
        this.name = name;
        this.count = count;
        this.isRequired = isRequired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    @Override
    public String toString() {
        return "Part{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", isRequired=" + isRequired +
                '}';
    }
}
