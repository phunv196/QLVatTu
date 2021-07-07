package com.app.model.category;

import com.app.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "quality")
public class QualityModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quality_id", unique = true, nullable = false) private Long qualityId;

    @Schema(example="601")
    @Column(name = "quality_code")       private String  qualityCode;
    @Column(name = "quality_name")      private String  qualityName;
    @Column(name = "description")       private String  description;

    //Getters and Setters
    public Long getQualityId() { return qualityId; }
    public void setQualityId(Long qualityId) { this.qualityId = qualityId; }

    public String getQualityCode() {
        return qualityCode;
    }

    public void setQualityCode(String qualityCode) {
        this.qualityCode = qualityCode;
    }

    public String getQualityName() {
        return qualityName;
    }

    public void setQualityName(String qualityName) {
        this.qualityName = qualityName;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static class QualityResponse extends PageResponse {
        private List<QualityModel> list;

        public List<QualityModel> getList() {return list; }
        public void setList(List<QualityModel> list) { this.list = list; }
    }
}
