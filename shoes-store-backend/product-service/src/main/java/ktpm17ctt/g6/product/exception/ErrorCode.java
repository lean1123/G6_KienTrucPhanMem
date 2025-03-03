package ktpm17ctt.g6.product.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),

    PRODUCT_NOT_FOUND(2001, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS(2002, "Product already exists", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_AVAILABLE(2003, "Product not available", HttpStatus.BAD_REQUEST),


    PRODUCT_ITEM_NOT_FOUND(2005, "Product item not found", HttpStatus.NOT_FOUND),
    PRODUCT_ITEM_ALREADY_EXISTS(2006, "Product item already exists", HttpStatus.BAD_REQUEST),
    PRODUCT_ITEM_NOT_AVAILABLE(2007, "Product item not available", HttpStatus.BAD_REQUEST),

    COLOR_NOT_FOUND(2008, "Color not found", HttpStatus.NOT_FOUND),
    COLOR_NAME_INVALID(2009, "Color name must be between 1 and 50 characters", HttpStatus.BAD_REQUEST),
    COLOR_CODE_INVALID(2010, "Color code must be between 1 and 7 characters", HttpStatus.BAD_REQUEST),

    CATEGORY_NOT_FOUND(2009, "Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS(2010, "Category already exists", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EMPTY(2011, "Category not empty", HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_INVALID(2012, "Category name must be between 3 and 50 characters", HttpStatus.BAD_REQUEST),

    PRODUCT_NAME_INVALID(2013, "Product name must be between 3 and 50 characters", HttpStatus.BAD_REQUEST),
    PRODUCT_DESCRIPTION_INVALID(2014, "Product description must be between 3 and 500 characters", HttpStatus.BAD_REQUEST),
    PRODUCT_RATING_INVALID(2015, "Product rating must be between 0 and 5", HttpStatus.BAD_REQUEST),
    PRODUCT_TYPE_INVALID(2016, "Product type must not be null", HttpStatus.BAD_REQUEST),
    PRODUCT_CATEGORY_INVALID(2017, "Product category id must not be null", HttpStatus.BAD_REQUEST),

    ITEM_PRICE_INVALID(2018, "Item price must be greater than 0", HttpStatus.BAD_REQUEST),
    ITEM_QUANTITY_INVALID(2019, "Item quantity must be greater than 0", HttpStatus.BAD_REQUEST),
    ITEM_PRODUCT_INVALID(2020, "Item product id must not be null", HttpStatus.BAD_REQUEST),
    ITEM_COLOR_INVALID(2021, "Item color id must not be null", HttpStatus.BAD_REQUEST),

    QUANTITY_NOT_NULL(2022, "Quantity must not be null", HttpStatus.BAD_REQUEST),
    SIZE_NOT_NULL(2023, "Size must not be null", HttpStatus.BAD_REQUEST),
    SIZE_INVALID(2024, "Size must be between 30 and 50", HttpStatus.BAD_REQUEST),

    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
