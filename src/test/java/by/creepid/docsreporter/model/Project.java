package by.creepid.docsreporter.model;

import by.creepid.docsreporter.context.annotations.Image;
import by.creepid.docsreporter.utils.ClassUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Project {

    private String name;
    private Date date;
    private BigDecimal price;
    private String author = null;
    private int stuffSize;
    private Manager manager;
    private List<DeveloperWithPhoto> developers;
    @Image(bookmarks = {"logo"}, width = 200)
    private byte[] logo;

    public Project() {
        developers = new ArrayList<>();
    }

    public Project(String name, Date date, BigDecimal price) {
        this();
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

    public List<DeveloperWithPhoto> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<DeveloperWithPhoto> developers) {
        this.developers = developers;
    }

    public void add(DeveloperWithPhoto developer) {
        developers.add(developer);
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
}
