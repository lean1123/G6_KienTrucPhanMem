package ktpm17ctt.g6.product.repository.custom;

import ktpm17ctt.g6.product.entity.ProductItem;
import ktpm17ctt.g6.product.entity.enums.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomProductItemRepositoryImpl implements CustomProductItemRepository {

    private final MongoTemplate mongoTemplate;

    public CustomProductItemRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<ProductItem> search(String productName, Type type, String categoryName, String colorName, Integer size, Double minPrice, Double maxPrice, Pageable pageable) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        // 🔹 Lọc theo tên sản phẩm
        if (productName != null && !productName.isEmpty()) {
            criteriaList.add(Criteria.where("product.name").regex(productName, "i")); // Không phân biệt hoa/thường
        }

        // 🔹 Lọc theo loại sản phẩm (Type)
        if (type != null) {
            criteriaList.add(Criteria.where("product.type").is(type));
        }

        // 🔹 Lọc theo tên danh mục (Category.name)
        if (categoryName != null && !categoryName.isEmpty()) {
            criteriaList.add(Criteria.where("product.category.name").regex(categoryName, "i")); // Không phân biệt hoa/thường
        }

        // 🔹 Lọc theo màu sắc (Color.name)
        if (colorName != null && !colorName.isEmpty()) {
            criteriaList.add(Criteria.where("color.name").regex(colorName, "i")); // Không phân biệt hoa/thường
        }

        // 🔹 Lọc theo kích thước (Size)
        if (size != null) {
            criteriaList.add(Criteria.where("quantityOfSize.size").is(size));
        }

        // 🔹 Lọc theo khoảng giá (minPrice ≤ price ≤ maxPrice)
        if (minPrice != null && maxPrice != null) {
            criteriaList.add(Criteria.where("price").gte(minPrice).lte(maxPrice));
        } else if (minPrice != null) {
            criteriaList.add(Criteria.where("price").gte(minPrice)); // Chỉ có min
        } else if (maxPrice != null) {
            criteriaList.add(Criteria.where("price").lte(maxPrice)); // Chỉ có max
        }

        // ✅ Áp dụng điều kiện tìm kiếm
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        // 🔹 Tính tổng số sản phẩm thỏa mãn
        long total = mongoTemplate.count(query, ProductItem.class);

        // 🔹 Áp dụng phân trang
        query.with(pageable);
        List<ProductItem> items = mongoTemplate.find(query, ProductItem.class);

        return new PageImpl<>(items, pageable, total);
    }
}
