package com.app.model.unit;

import com.app.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "unit")
public class UnitModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id", unique = true, nullable = false) private Long unitId;

    @Schema(example="601")
    @Column(name = "code")       private String  code;
    @Column(name = "name")      private String  name;
    @Column(name = "description")       private String  description;

    //Getters and Setters


    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
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

    public static class UnitResponse extends PageResponse {
        private List<UnitModel> list;

        public List<UnitModel> getList() {return list; }
        public void setList(List<UnitModel> list) { this.list = list; }
    }
}
