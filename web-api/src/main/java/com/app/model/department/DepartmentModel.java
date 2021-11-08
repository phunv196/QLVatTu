package com.app.model.department;

import com.app.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "department")
public class DepartmentModel {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id", unique = true, nullable = false) private Long departmentId;

    @Schema(example="601")
    @Column(name = "code")       private String  code;
    @Column(name = "name")      private String  name;
    @Column(name = "email")      private String email;
    @Column(name = "phone")       private String  phone;
    @Column(name = "address")       private String  address;
    @Column(name = "description")       private String  description;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class DepartmentResponse extends PageResponse {
        private List<DepartmentModel> list;

        public List<DepartmentModel> getList() {return list; }
        public void setList(List<DepartmentModel> list) { this.list = list; }
    }

}
