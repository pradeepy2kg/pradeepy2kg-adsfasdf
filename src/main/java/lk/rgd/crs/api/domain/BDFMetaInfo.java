package lk.rgd.crs.api.domain;

import javax.persistence.Embeddable;

import javax.persistence.Embeddable;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Java bean instance to capture specific meta info for a BirthDeclaration. Info on confirmation_approved, printed, data entered events.
 *  */
@Embeddable
public class BDFMetaInfo extends BaseMetaInfo implements Serializable {
    /**
     * Confirmation changes Approved Date
     */
    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date confirmationlastUpdatedDate;

    /**
     * Confirmeation changes approved user
     */
    @Column(nullable = false)
    private long userId;

}
