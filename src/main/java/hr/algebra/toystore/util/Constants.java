package hr.algebra.toystore.util;

public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Constants is a utility class.");
    }

    public static final String REDIRECT_DASHBOARD = "redirect:/admin/dashboard";
    public static final String ERROR_ATTRIBUTE = "error";

    public static final String ORDERS = "orders";

    public static final String REDIRECT_CART = "redirect:/cart";

    public static final String CATEGORIES = "categories";
    public static final String SELECTED_CATEGORY_ID = "selectedCategoryId";

    public static final String CART_NOT_FOUND_MESSAGE = "Cart not found.";

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
}
