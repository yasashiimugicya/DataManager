package com.example.datamanager01;

import java.io.Serializable;

public class Product implements Serializable {
    private int id; //ID
    private String name; //蝠・刀蜷・
    private int price; //蜊倅ｾ｡

    //繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ
    public Product() {}

    public Product(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    //繧ｻ繝・ち・・ご繝・ち
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
