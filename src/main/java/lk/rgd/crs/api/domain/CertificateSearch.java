package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * An instance represents information submitted for the certificate search for the life events birth, death etc.
 *
 * @author Chathuranga Withana
 */
@Entity
@Table(name = "CERTIFICATE_SEARCH", schema = "CRS",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"dsDivisionUKey", "applicationNo"})})

@NamedQueries({
    @NamedQuery(name = "get.by.applicationNo.and.dsDivision", query = "SELECT cs FROM CertificateSearch cs " +
        "WHERE cs.certificate.applicationNo = :applicationNo AND cs.certificate.dsDivision = :dsDivision")
})
public class CertificateSearch implements Serializable {

    /**
     * The Enumeration defining the type of certificate
     */
    public enum CertificateType {
        /**
         * 0 - A Birth Certificate
         */
        BIRTH,
        /**
         * 1 - A Death Certificate
         */
        DEATH
    }

    /**
     * This is the auto generated unique row identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    @Embedded
    private CertificateInfo certificate = new CertificateInfo();

    @Embedded
    private SearchInfo search = new SearchInfo();

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public CertificateInfo getCertificate() {
        if (certificate == null) {
            certificate = new CertificateInfo();
        }
        return certificate;
    }

    public void setCertificate(CertificateInfo certificate) {
        this.certificate = certificate;
    }

    public SearchInfo getSearch() {
        if (search == null) {
            search = new SearchInfo();
        }
        return search;
    }

    public void setSearch(SearchInfo search) {
        this.search = search;
    }
}