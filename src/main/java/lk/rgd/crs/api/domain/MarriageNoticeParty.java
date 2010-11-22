package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.District;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * embeddable class for  notice party(male or female)
 * todo this class is not complete
 */
@Embeddable
public class MarriageNoticeParty implements Serializable, Cloneable {
    @Column(name = "IDENTIFICATION_NUMBER", nullable = true)
    //pin or nic
    private String identificationNumber;

    @Column(name = "DOB", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "AGE_LAST_BD", nullable = true)
    private int ageAtLastBirthDay;

    @Column(name = "NAME_OFFICIAL", nullable = true, length = 600)
    private String nameInOfficialLanguage;

    @Column(name = "NAME_ENGLISH",nullable = true,length = 600)
    private String nameInEnglish;

    @Column(name = "ADDRESS",nullable = true,length = 255)
    private String residentAddress;
/*
    @ManyToOne
    @JoinColumn()
    private District disti*/
}

