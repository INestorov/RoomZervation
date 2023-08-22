package application.entities;

import java.time.LocalDate;
import java.util.Objects;

public class Holiday {

    private int id;
    private String name;
    private LocalDate start;
    private LocalDate end;
    private boolean repetitive;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Holiday holiday = (Holiday) o;
        return repetitive == holiday.repetitive
            && name.equals(holiday.name)
            && start.equals(holiday.start)
            && end.equals(holiday.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, start, end, repetitive);
    }

    /**
     * Constructor.
     *
     * @param name       name of holiday
     * @param start      start day
     * @param end        end day
     * @param repetitive if event repeats every year
     */
    public Holiday(String name, LocalDate start, LocalDate end, boolean repetitive) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.repetitive = repetitive;
    }

    /**
     * Constructor if isRepetitive is not known.
     *
     * @param id         id of holiday
     * @param name       name of holiday
     * @param start      start day
     * @param end        end day
     */
    public Holiday(int id, String name, LocalDate start, LocalDate end) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public boolean isRepetitive() {
        return repetitive;
    }

    public void setRepetitive(boolean repetitive) {
        this.repetitive = repetitive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
