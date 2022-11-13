package door.one.marketplace;

public class Products {
    private String pname,description,image,category,prodid,date,time,price;

    public Products(){

    }

    public Products(String pname, String description, String image, String category, String prodid, String date, String time,String price) {
        this.pname = pname;
        this.description = description;
        this.image = image;
        this.category = category;
        this.prodid = prodid;
        this.date = date;
        this.time = time;
        this.price=price;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String prodname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProdid() {
        return prodid;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
