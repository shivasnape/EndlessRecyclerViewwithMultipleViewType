package com.shivichu.recyclerviewwithviewtype.model;

public class Model {

    public static final int TYPE_LOADING=0;
    public static final int TYPE_1=1;
    public static final int TYPE_2=2;
    public static final int TYPE_3=3;

    public int type;
    public int data;
    public String text;

    public Model(){}

    public Model(int type, String text, int data)
    {
        this.type=type;
        this.data=data;
        this.text=text;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
