package com.example.drunkshop;

public class Drink {
    private String id;
    private String name;
    private String info;
    private String price;
    private float rating;
    private int image;
    private int count;

    public Drink() { }

    public Drink(String name, String info, String price, float rating, int image, int count) {
        this.name = name;
        this.info = info;
        this.price = price;
        this.rating = rating;
        this.image = image;
        this.count = count;
    }

    public String getName() { return name; }

    public String getInfo() { return info; }

    public String getPrice() { return price; }

    public float getRating() { return rating; }

    public int getImage() { return image; }

    public int getCount() { return count; }

    public String _getId(){ return id;}

    public void setId(String id){
        this.id = id;
    }
}
