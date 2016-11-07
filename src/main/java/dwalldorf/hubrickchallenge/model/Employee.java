package dwalldorf.hubrickchallenge.model;

import java.math.BigDecimal;

public class Employee {

    private String fullName;

    private Department department;

    private String sex;

    private Integer age;

    private BigDecimal salary;

    public Employee() {
    }

    public Employee(String fullName, Department department, String sex, Integer age, BigDecimal salary) {
        this.fullName = fullName;
        this.department = department;
        this.sex = sex;
        this.age = age;
        this.salary = salary;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
