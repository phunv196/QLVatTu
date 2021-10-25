package com.app.model.employee;

import com.app.model.PageResponse;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "employees")
public class EmployeeModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", unique = true, nullable = false) private Long employeeId;
    @Column(name = "code") private String  code;
    @Column(name = "last_name") private String  lastName;
    @Column(name = "first_name") private String  firstName;
    @Column(name = "full_name") private String  fullName;
    @Column(name = "email") private String  email;
    @Column(name = "phone") private String  phone;
    @Column(name = "address") private String  address;
    @Column(name = "sex") private Long  sex;
    @Column(name = "birth") private Date birth;
    @Column(name = "position_id") private Long positionId;
    @Column(name = "department_id") private Long  departmentId;


    @Transient
    private String positionName;

    @Transient
    private String departmentName;

    @Transient
    private String sexString;

    public EmployeeModel(){};

    public EmployeeModel(Long employeeId, String code, String lastName, String firstName, String fullName,
                         String email, String phone, String address, Long sex, Date birth,
                         Long positionId, Long departmentId, String positionName, String departmentName) {
        this.employeeId = employeeId;
        this.code = code;
        this.lastName = lastName;
        this.firstName = firstName;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.sex = sex;
        this.birth = birth;
        this.positionId = positionId;
        this.departmentId = departmentId;
        this.positionName = positionName;
        this.departmentName = departmentName;
    }

// Getter and Setters


    public String getSexString() {
        return sexString;
    }

    public void setSexString(String sexString) {
        this.sexString = sexString;
    }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Long getSex() { return sex; }
    public void setSex(Long sex) { this.sex = sex; }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public static class EmployeeResponse extends PageResponse {
        private List<EmployeeModel> list;

        public List<EmployeeModel> getList() {return list; }
        public void setList(List<EmployeeModel> list) { this.list = list; }
    }
}
