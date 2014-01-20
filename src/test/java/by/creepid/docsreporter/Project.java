package by.creepid.docsreporter;

import java.math.BigDecimal;
import java.util.Date;

public class Project {

    private String name;
    private Date date;
    private BigDecimal price;
    private String author = null;
    private int stuffSize;
    private Manager manager;

    public Project() {
    }

    public Project(String name, Date date, BigDecimal price) {
        this.name = name;
        this.date = date;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getStuffSize() {
        return stuffSize;
    }

    public void setStuffSize(int stuffSize) {
        this.stuffSize = stuffSize;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
