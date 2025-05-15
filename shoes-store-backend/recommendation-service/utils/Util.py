def get_price_range(price):
    if price <= 300000:
        return "giá_thấp"
    elif price <= 600000:
        return "giá_trung_bình"
    else:
        return "giá_cao"

def extract_product_fields(prod):
    product = prod.get("product", {})
    name = product.get("name", "")
    category = product.get("category", {}).get("name", "")
    color = prod.get("color", {}).get("name", "")
    type_ = product.get("type", "")
    price = prod.get("price", 0)
    price_range = get_price_range(price)

    return {
        "name": name,
        "category": category,
        "color": color,
        "type": type_,
        "price_range": price_range
    }

def build_text(prod):
    fields = extract_product_fields(prod)
    return " ".join([
        fields["name"],
        fields["category"],
        fields["color"],
        fields["type"],
        fields["price_range"]
    ]).strip()
