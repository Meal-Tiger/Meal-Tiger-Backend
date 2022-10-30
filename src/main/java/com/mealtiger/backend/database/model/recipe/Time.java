package com.mealtiger.backend.database.model.recipe;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This class serves as a model for the time span a recipe takes to follow.
 *
 * @author Lucca Greschner, Sebastian Maier
 */
public class Time {

    private int minimum;
    private String minimumUnit;

    private int maximum;
    private String maximumUnit;

    /**
     * This is a constructor for Time.
     *
     * @param minimum     minimum amount of time user set.
     * @param minimumUnit unit user chose for minimum amount of time.
     * @param maximum     maximum amount of time user set.
     * @param maximumUnit unit user chose for maximum amount of time.
     */
    public Time(int minimum, String minimumUnit, int maximum, String maximumUnit) {
        this.minimum = minimum;
        this.minimumUnit = minimumUnit;

        this.maximum = maximum;
        this.maximumUnit = maximumUnit;
    }

    /**
     * This is a constructor for Time.
     *
     * @param amount time user set.
     * @param unit   unit of time user set.
     */
    public Time(int amount, String unit) {
        this(amount, unit, 0, null);
    }

    /**
     * This is a constructor for Time.
     */
    @SuppressWarnings("unused")
    public Time() {
        this(0, null);
    }

    // SETTER

    /**
     * This method sets the minimum unit user chose to the local variable.
     *
     * @param minimumUnit unit user chose for minimum amount of time.
     */
    @SuppressWarnings("unused")
    public void setMinimumUnit(String minimumUnit) {
        this.minimumUnit = minimumUnit;
    }

    /**
     * This method sets the minimum amount of time user chose to the local variable.
     *
     * @param minimum minimum amount of time user set.
     */
    @SuppressWarnings("unused")
    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    /**
     * This method sets the maximum amount of time user chose to the local variable.
     *
     * @param maximum maximum amount of time user set.
     */
    @SuppressWarnings("unused")
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = TimeFilter.class)
    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    /**
     * This method sets unit user chose for maximum amount of time.
     *
     * @param maximumUnit unit user chose for maximum amount of time.
     */
    @SuppressWarnings("unused")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public void setMaximumUnit(String maximumUnit) {
        this.maximumUnit = maximumUnit;
    }

    // GETTER

    /**
     * This method returns the minimum amount of time that user chose.
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * This method returns the unit that the user chose for minimum amount of time.
     */
    public String getMinimumUnit() {
        return minimumUnit;
    }

    /**
     * This method returns the maximum amount of time that user chose.
     */
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = TimeFilter.class)
    public int getMaximum() {
        return maximum;
    }

    /**
     * This method returns the unit that the user chose for maximum amount of time.
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public String getMaximumUnit() {
        return maximumUnit;
    }

    /**
     * Filter to exclude maximum time from serialization when it is not needed (i.e. if it is 0).
     */
    static class TimeFilter {
        /**
         * This method checks if the Object is an Integer and equals to 0.
         *
         * @see <a href="https://javadoc.io/static/com.fasterxml.jackson.core/jackson-annotations/2.14.0-rc3/com/fasterxml/jackson/annotation/JsonInclude.html#valueFilter--">Jackson Javadoc</a>
         */
        @Override
        public boolean equals(Object o) {
            return o instanceof Integer && o.equals(0);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Time time = (Time) o;

        if (getMinimum() != time.getMinimum()) return false;
        if (getMaximum() != time.getMaximum()) return false;
        if (!getMinimumUnit().equals(time.getMinimumUnit())) return false;
        return getMaximumUnit() != null ? getMaximumUnit().equals(time.getMaximumUnit()) : time.getMaximumUnit() == null;
    }

    @Override
    public int hashCode() {
        int result = getMinimum();
        result = 31 * result + getMinimumUnit().hashCode();
        result = 31 * result + getMaximum();
        result = 31 * result + (getMaximumUnit() != null ? getMaximumUnit().hashCode() : 0);
        return result;
    }
}
