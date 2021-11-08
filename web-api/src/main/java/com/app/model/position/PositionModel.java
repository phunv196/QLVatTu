package com.app.model.position;

import com.app.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "position")
public class PositionModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id", unique = true, nullable = false) private Long positionId;

    @Schema(example="601")
    @Column(name = "code")       private String  code;
    @Column(name = "name")      private String  name;
    @Column(name = "description")       private String  description;

    //Getters and Setters

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
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

    public static class PositionResponse extends PageResponse {
        private List<PositionModel> list;

        public List<PositionModel> getList() {return list; }
        public void setList(List<PositionModel> list) { this.list = list; }
    }
}
