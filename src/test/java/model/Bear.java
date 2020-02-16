package model;

import com.google.gson.annotations.SerializedName;

public class Bear {
    @SerializedName("bear_name")
    private String name;

    @SerializedName("bear_age")
    private Double age;

    @SerializedName("bear_type")
    private String type;

    @SerializedName("bear_id")
    private String id;

    public String getName() {
        return name;
    }

    public Double getAge() {
        return age;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Bear(String type, String name, Double age) {
        this.type = type;
        this.name = name;
        this.age = age;
    }
}