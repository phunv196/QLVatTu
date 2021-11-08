package com.app.model.role;

import com.app.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
public class RoleModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", unique = true, nullable = false) private Long roleId;

    @Schema(example="601")
    @Column(name = "code")       private String  code;
    @Column(name = "name")      private String  name;
    @Column(name = "description")       private String  description;

    //Getters and Setters


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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

    public static class RolesResponse extends PageResponse {
        private List<RoleModel> list;

        public List<RoleModel> getList() {return list; }
        public void setList(List<RoleModel> list) { this.list = list; }
    }
}
