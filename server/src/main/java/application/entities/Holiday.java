package application.entities;

import com.sun.istack.NotNull;
import java.sql.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "holiday")
public class Holiday {

    @Id
    private int id;

    @NotNull
    @Column(name = "start")
    private Date start;

    @NotNull
    @Column(name = "end")
    private Date end;

    @NotNull
    @Column(name = "name")
    private String name;

    public Holiday() {
    }

    /**
     * Constructor.
     *
     * @param name       name of holiday
     * @param start      start day
     * @param end        end day
     * @param repetitive if event repeats every year
     */
    public Holiday(String name, Date start, Date end, boolean repetitive) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Holiday holiday = (Holiday) o;
        return id == holiday.id
            && Objects.equals(start, holiday.start)
            && Objects.equals(end, holiday.end);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

}
