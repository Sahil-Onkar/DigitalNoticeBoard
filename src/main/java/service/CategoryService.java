package service;

import dao.CategoryDAO;
import model.Category;

import java.util.List;

public class CategoryService {

    private CategoryDAO categoryDAO;

    public CategoryService() {
        categoryDAO = new CategoryDAO();
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }
    public String getCategoryNameById(int categoryId) {

        List<Category> categories =
                categoryDAO.getAllCategories();

        for (Category category : categories) {

            if (category.getCategoryId() == categoryId) {
                return category.getCategoryName();
            }
        }

        return "Unknown";
    }
}
