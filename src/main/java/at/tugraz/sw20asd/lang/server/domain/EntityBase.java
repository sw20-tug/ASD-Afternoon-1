package at.tugraz.sw20asd.lang.server.domain;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Version
    protected Long version;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date creationDate;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public Long getVersion() {
        return this.version;
    }
}
