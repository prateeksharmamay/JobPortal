package com.apply.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Vivek Agarwal on 5/9/2017.
 */
@Entity
@Table(name="company")
public class Company {
    @Id
    @Column(name = "email")
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String website;

    @Column
    private String imageUrl;

    @Column
    private String address;

    @Column
    private String description;

    @Column
    private boolean verified;

    @Column
    private String hashValue;

    @OneToMany(mappedBy = "company")
    private List<Opening> openings;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageURL(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public List<Opening> getOpenings() {
        return openings;
    }

    public void setOpenings(List<Opening> openings) {
        this.openings = openings;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isVerified(){
        return verified;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

   /* @Override
    public String toString() {
        return "Company{" +
                ", email'" + email+ '\'' +
                ", name='" + name + '\'' +
                ", website='" + website + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                '}';
    }*/

}
