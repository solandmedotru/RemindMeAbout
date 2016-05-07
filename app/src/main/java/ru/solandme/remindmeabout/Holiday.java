package ru.solandme.remindmeabout;

import java.io.Serializable;
import java.util.Comparator;

public class Holiday implements Serializable{

    public static final String CATEGORY_HOLIDAY = "holiday";
    public static final String CATEGORY_BIRTHDAY = "birthday";
    public static final String CATEGORY_EVENT = "event";
    public static final String CODE_WOMANSDAY = "womansday";
    public static final String CODE_MANSDAY = "mansday";


    private String id;
    private String name;
    private String description;
    private String imageUri;
    private String category;
    private int daysLeft;
    private Long date;
    private String code;

    public Holiday() {
    }

    public Holiday(String id, String name, String description, String imageUri, String category, int daysLeft, Long date, String code) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUri = imageUri;
        this.category = category;
        this.daysLeft = daysLeft;
        this.date = date;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    static Comparator<Holiday> daysOrdered = new Comparator<Holiday>() {
        @Override
        public int compare(Holiday lhs, Holiday rhs) {
            if(lhs.daysLeft < rhs.daysLeft){
                return -1;
            } else if(lhs.daysLeft > rhs.daysLeft){
                return 1;
            }
            return 0;
        }
    };

    static Comparator<Holiday> nameOrdered = new Comparator<Holiday>() {
        @Override
        public int compare(Holiday lhs, Holiday rhs) {
            return lhs.name.compareTo(rhs.name);
        }
    };

}
