package com.app.model.category;

import com.app.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
public class CategoryModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", unique = true, nullable = false) private Long categoryId;

    @Schema(example="601")
    @Column(name = "code")       private String  code;
    @Column(name = "parent_code")       private String  parentCode;
    @Column(name = "name")      private String  name;
    @Column(name = "description")       private String  description;

    //Getters and Setters
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static class CategoryResponse extends PageResponse {
        private List<CategoryModel> list;

        public List<CategoryModel> getList() {return list; }
        public void setList(List<CategoryModel> list) { this.list = list; }
    }
}
