package com.app.model.employee;

import com.app.model.PageResponse;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "employees")
public class EmployeeModel {
    @Id @GeneratedValue private Integer id;
    @Column(name = "code") private String  code;
    @Column(name = "last_name") private String  lastName;
    @Column(name = "first_name") private String  firstName;
    @Column(name = "full_name") private String  fullName;
    @Column(name = "email") private String  email;
    @Column(name = "avatar") private String  avatar;
    @Column(name = "job_title") private String  jobTitle;
    @Column(name = "department") private String  department;
    @Column(name = "manager_id") private Integer managerId;
    @Column(name = "phone") private String  phone;
    @Column(name = "address1") private String  address1;
    @Column(name = "address2") private String  address2;
    @Column(name = "city") private String  city;
    @Column(name = "state") private String  state;
    @Column(name = "postal_code") private String  postalCode;
    @Column(name = "country") private String  country;
    @Column(name = "position_id") private Integer positionId;



    //Constructors
    public EmployeeModel(){}
    public EmployeeModel(Integer id, String code, String lastName, String firstName, String fullName,
                         String email, String avatar, String jobTitle, String department, Integer managerId,
                         String phone, String address1, String address2, String city, String state,
                         String postalCode, String country, Integer positionId) {
        this.id = id;
        this.code = code;
        this.lastName = lastName;
        this.firstName = firstName;
        this.fullName = fullName;
        this.email = email;
        this.avatar = avatar;
        this.jobTitle = jobTitle;
        this.department = department;
        this.managerId = managerId;
        this.phone = phone;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.positionId = positionId;
    }

    // Getter and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

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

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public static class EmployeeResponse extends PageResponse {
        private List<EmployeeModel> list;

        public List<EmployeeModel> getList() {return list; }
        public void setList(List<EmployeeModel> list) { this.list = list; }
    }
}
