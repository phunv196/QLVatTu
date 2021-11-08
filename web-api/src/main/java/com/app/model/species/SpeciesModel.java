package com.app.model.species;

import com.app.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "species")
public class SpeciesModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "species_id", unique = true, nullable = false) private Long speciesId;

    @Schema(example="601")
    @Column(name = "code")       private String  code;
    @Column(name = "name")      private String  name;
    @Column(name = "description")       private String  description;

    //Getters and Setters


    public Long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Long speciesId) {
        this.speciesId = speciesId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class SpeciesResponse extends PageResponse {
        private List<SpeciesModel> list;

        public List<SpeciesModel> getList() {return list; }
        public void setList(List<SpeciesModel> list) { this.list = list; }
    }
}
