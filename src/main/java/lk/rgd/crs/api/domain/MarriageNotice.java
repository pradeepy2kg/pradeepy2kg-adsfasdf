package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author amith jayasekara
 *         entity class for marriage notice
 *         note:this class is not complete
 */
@Entity
@Table(name = "MARRIAGE_REGISTER", schema = "CRS")
public class MarriageNotice implements Serializable, Cloneable {
    public enum PlaceOfMarriage {
        REGISTRAR_OFFICE,
        DS_OFFICE,
        CHURCH,
        OTHER
    }

    public enum TypeOfMarriage {
        GENERAL,
        KANDYAN_BINNA,
        KANDYAN_DEEGA
    }

    @Id
    @GeneratedValue
    private long idUKey;

    @Column(name = "SERIAL_NUMBER", nullable = false)
    private Long serialNumber;

    @Column(name = "RECEIVED_DATE", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date receivedDate;
}
