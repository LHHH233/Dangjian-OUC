package com.partymember.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "party_members")
public class PartyMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "serial_number")
    private String serialNumber;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "ethnic_group")
    private String ethnicGroup;
    
    @Column(name = "political_status")
    private String politicalStatus;
    
    @Column(name = "join_date")
    private LocalDate joinDate;
    
    @Column(name = "id_card_number", unique = true)
    private String idCardNumber;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Column(name = "birthplace")
    private String birthplace;
    
    @Column(name = "residence")
    private String residence;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "admission_date")
    private LocalDate admissionDate;
    
    @Column(name = "degree")
    private String degree;
    
    @Column(name = "major")
    private String major;
    
    @Column(name = "class_of_year")
    private String classOfYear;
    
    @Column(name = "organization")
    private String organization;
    
    // 构造函数
    public PartyMember() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSerialNumber() {
        return serialNumber;
    }
    
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getEthnicGroup() {
        return ethnicGroup;
    }
    
    public void setEthnicGroup(String ethnicGroup) {
        this.ethnicGroup = ethnicGroup;
    }
    
    public String getPoliticalStatus() {
        return politicalStatus;
    }
    
    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus;
    }
    
    public LocalDate getJoinDate() {
        return joinDate;
    }
    
    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }
    
    public String getIdCardNumber() {
        return idCardNumber;
    }
    
    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getBirthplace() {
        return birthplace;
    }
    
    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }
    
    public String getResidence() {
        return residence;
    }
    
    public void setResidence(String residence) {
        this.residence = residence;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public LocalDate getAdmissionDate() {
        return admissionDate;
    }
    
    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }
    
    public String getDegree() {
        return degree;
    }
    
    public void setDegree(String degree) {
        this.degree = degree;
    }
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public String getClassOfYear() {
        return classOfYear;
    }
    
    public void setClassOfYear(String classOfYear) {
        this.classOfYear = classOfYear;
    }
    
    public String getOrganization() {
        return organization;
    }
    
    public void setOrganization(String organization) {
        this.organization = organization;
    }
}




