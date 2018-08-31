package com.example.treemenu;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author yaxi
 * @date 2018/8/30
 */

@Entity
public class Data {

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private double price;
    private Long belongTo;
    @Transient
    private int selected;
    @Generated(hash = 1945136477)
    public Data(Long id, String name, double price, Long belongTo) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.belongTo = belongTo;
    }
    @Generated(hash = 2135787902)
    public Data() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public Long getBelongTo() {
        return this.belongTo;
    }
    public void setBelongTo(Long belongTo) {
        this.belongTo = belongTo;
    }


    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", belongTo=" + belongTo +
                ", selected=" + selected +
                '}';
    }
}
