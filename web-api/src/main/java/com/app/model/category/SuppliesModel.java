package com.app.model.category;

import com.app.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "supplies")
public class SuppliesModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplies_id", unique = true, nullable = false) private Long suppliesId;

    @Schema(example="601")
    @Column(name = "code")       private String  code;
    @Column(name = "name")      private String  name;
    @Column(name = "description")       private String  description;
    @Column(name = "price")      private Long  price;
    @Column(name = "species_id")      private Long  speciesId;
    @Column(name = "quality_id")      private Long  qualityId;
    @Column(name = "supplier_id")      private Long  supplierId;
    @Column(name = "unit_id")      private Long  unitId;

    @Transient
    private String speciesName;

    @Transient
    private String qualityName;

    @Transient
    private String supplierName;

    @Transient
    private String unitName;

    @Transient
    private Long formPrice;

    @Transient
    private Long toPrice;

    //Getters and Setters


    public Long getFormPrice() {
        return formPrice;
    }

    public void setFormPrice(Long formPrice) {
        this.formPrice = formPrice;
    }

    public Long getToPrice() {
        return toPrice;
    }

    public void setToPrice(Long toPrice) {
        this.toPrice = toPrice;
    }

    public Long getSuppliesId() {
        return suppliesId;
    }

    public void setSuppliesId(Long suppliesId) {
        this.suppliesId = suppliesId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Long speciesId) {
        this.speciesId = speciesId;
    }

    public Long getQualityId() {
        return qualityId;
    }

    public void setQualityId(Long qualityId) {
        this.qualityId = qualityId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getQualityName() {
        return qualityName;
    }

    public void setQualityName(String qualityName) {
        this.qualityName = qualityName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public static class SuppliesResponse extends PageResponse {
        private List<SuppliesModel> list;

        public List<SuppliesModel> getList() {return list; }
        public void setList(List<SuppliesModel> list) { this.list = list; }
    }
}
