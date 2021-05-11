package com.waracle.cakemgr.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DynamicUpdate
@Table(name = "Cake", uniqueConstraints = {@UniqueConstraint(columnNames = "ID"), @UniqueConstraint(columnNames = "TITLE")})
public class CakeEntity implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false)
    private Long cakeId;

    @Column(name = "TITLE", unique = true, nullable = false, length = 100)
    private String title;

    @JsonProperty("desc")
    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;

    @Column(name = "IMAGE", nullable = false, length = 300)
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getCakeId() {
        return cakeId;
    }

    public void setCakeId(Long cakeId) {
        this.cakeId = cakeId;
    }
}