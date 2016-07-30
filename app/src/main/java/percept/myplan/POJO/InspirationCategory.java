package percept.myplan.POJO;

/**
 * Created by percept on 20/7/16.
 */

public class InspirationCategory {

    private String CategoryId;
    private String CategoryName;


    public InspirationCategory(String catId, String catName) {
        this.CategoryId = catId;
        this.CategoryName = catName;

    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }


}
