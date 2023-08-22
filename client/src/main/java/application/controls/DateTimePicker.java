package application.controls;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * A DateTimePicker with configurable datetime format where both date and time can be changed
 * via the text field and the date can additionally be changed via the JavaFX default date picker.
 */
//@author - Silviu Marii
@SuppressWarnings("unused")
public class DateTimePicker extends DatePicker {
    private static final String CSS_CALENDAR_FOOTER = "calendar-footer";
    private static final String CSS_CALENDAR = "calendar";
    private static final String CSS_CALENDAR_TODAY_BUTTON = "calendar-today-button";
    public static final String DefaultFormat = "yyyy-MM-dd";

    private DateTimeFormatter formatter;
    private ObjectProperty<LocalDateTime> dateTimeValue =
        new SimpleObjectProperty<>(LocalDateTime.now());
    private ObjectProperty<String> format = new SimpleObjectProperty<String>() {
        public void set(String newValue) {
            super.set(newValue);
            formatter = DateTimeFormatter.ofPattern(newValue);
        }
    };

    /**
     * Create a new DateTimePicker instance.
     */
    public DateTimePicker() {

        HBox todayButtonBox = new HBox();
        todayButtonBox.getStyleClass().add(CSS_CALENDAR_FOOTER);

        Button todayButton = new Button();
        todayButton.textProperty().bind(todayButtonText);
        todayButton.getStyleClass().add(CSS_CALENDAR_TODAY_BUTTON);
        getStyleClass().add(CSS_CALENDAR);
        //getStyleClass().add("datetime-picker");
        todayButtonBox.setAlignment(Pos.CENTER);
        getChildren().add(todayButtonBox);
        todayButtonBox.getChildren().add(todayButton);
        setFormat(DefaultFormat);
        setConverter(new InternalConverter());
        // Syncronize changes to the underlying date value back to the dateTimeValue
        valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                dateTimeValue.set(null);
            } else {
                if (dateTimeValue.get() == null) {
                    dateTimeValue.set(LocalDateTime.of(newValue, LocalTime.now()));
                } else {
                    LocalTime time = dateTimeValue.get().toLocalTime();
                    dateTimeValue.set(LocalDateTime.of(newValue, time));
                }
            }
        });
        // Syncronize changes to dateTimeValue back to the underlying date value
        dateTimeValue.addListener((observable, oldValue, newValue) -> {
            setValue(newValue == null ? null : newValue.toLocalDate());
        });
        // Persist changes onblur
        getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                simulateEnterPressed();
            }
        });
    }

    public StringProperty todayButtonTextProperty() {
        return todayButtonText;
    }

    private StringProperty todayButtonText = new SimpleStringProperty("Today");

    public String getTodayButtonText() {
        return todayButtonText.get();
    }

    public void setTodayButtonText(String todayButtonText) {
        this.todayButtonText.set(todayButtonText);
    }

    private void simulateEnterPressed() {
        getEditor().fireEvent(new KeyEvent(getEditor(), getEditor(), KeyEvent.KEY_PRESSED, null,
            null, KeyCode.ENTER, false, false, false, false));
    }

    public LocalDateTime getDateTimeValue() {
        return dateTimeValue.get();
    }

    public void setDateTimeValue(LocalDateTime dateTimeValue) {
        this.dateTimeValue.set(dateTimeValue);
    }

    /**
     * Formats date and time as 'E.d.M.y H:m'.
     *
     * @return formatted string
     */
    public String toStringTime() {
        DateTimeFormat retrieve = new DateTimeFormat(dateTimeValue.get().getDayOfWeek(),
            dateTimeValue.get().getDayOfMonth(),
            dateTimeValue.get().getMonthValue(), dateTimeValue.get().getYear(),
            dateTimeValue.get().getHour(), dateTimeValue.get().getMinute());
        return retrieve.toString();
    }

    public ObjectProperty<LocalDateTime> dateTimeValueProperty() {
        return dateTimeValue;
    }

    public String getFormat() {
        return format.get();
    }

    public void setFormat(String format) {
        this.format.set(format);
    }

    public ObjectProperty<String> formatProperty() {
        return format;
    }

    public static class DateTimeFormat {
        private DayOfWeek dayOfWeek;
        private int hour;
        private int minute;
        private int month;
        private int year;
        private int day;

        /**
         * Create a new DateTimeFormat.
         *
         * @param dayOfWeek day of week
         * @param day       day
         * @param month     month
         * @param year      year
         * @param hour      hour
         * @param minute    minute
         */
        public DateTimeFormat(DayOfWeek dayOfWeek, int day, int month, int year, int hour,
                              int minute) {
            this.dayOfWeek = dayOfWeek;
            this.hour = hour;
            this.minute = minute;
            this.day = day;
            this.month = month;
            this.year = year;
            //   this.dayOfWeek=this.getDayOfWeek().getDisplayName();
        }


        public String getDate() {
            return " " + this.dayOfWeek + " " + this.day + "." + this.month + "." + this.year;
        }

        public String getTime() {
            return this.hour + ":" + this.minute;
        }

        public String toString() {
            return this.getDate(); // + " " + this.getTime();
        }

        public DayOfWeek getDayOfWeek() {
            return dayOfWeek;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }

        public int getMonth() {
            return month;
        }

        public int getYear() {
            return year;
        }

        public int getDay() {
            return day;
        }
    }

    private class InternalConverter extends StringConverter<LocalDate> {
        public String toString(LocalDate object) {
            LocalDateTime value = getDateTimeValue();
            return (value != null) ? value.format(formatter) : "";
        }

        public LocalDate fromString(String value) {
            if (value == null) {
                dateTimeValue.set(null);
                return null;
            }

            dateTimeValue.set(LocalDateTime.parse(value, formatter));
            return dateTimeValue.get().toLocalDate();
        }
    }
}