from pymongo import MongoClient
import joblib
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np

# Kết nối MongoDB
MONGO_URI = "mongodb+srv://hhglorious:eDXWxIAwnWJkxBTH@cluster0.8sxd9.mongodb.net/?retryWrites=true&w=majority"
client = MongoClient(MONGO_URI)
db = client["shoes-store"]
product_items = db["product-item"]

# Load mô hình đã huấn luyện
model_data = joblib.load("model.pkl")
tfidf_matrix = model_data["tfidf_matrix"]
product_ids = model_data["product_ids"]
vectorizer = model_data["vectorizer"]

# Hàm trích xuất văn bản đặc trưng từ sản phẩm
def build_text(prod):
    product = prod.get("product", {})
    name = product.get("name", "")
    category = product.get("category", {}).get("name", "")
    color = prod.get("color", {}).get("name", "")
    type_ = product.get("type", "")  # MALE/FEMALE/UNISEX
    price = prod.get("price", "")
    return f"{name} {category} {color} {type_} {price}".strip()

# Hàm gợi ý sản phẩm dựa vào sản phẩm user đã like
def get_recommendations(user_id, top_n=5):
    liked = list(product_items.find({"likes": user_id}))
    if not liked:
        print(f"[INFO] User {user_id} chưa like sản phẩm nào.")
        return []

    liked_texts = [build_text(item) for item in liked if build_text(item).strip()]

    if not liked_texts:
        print(f"[WARN] Không có văn bản đặc trưng từ sản phẩm user đã like.")
        return []

    # Vector hóa văn bản sản phẩm user đã thích
    liked_vectors = vectorizer.transform(liked_texts)
    similarity_scores = cosine_similarity(liked_vectors, tfidf_matrix)

    print(similarity_scores)


    # Trung bình độ tương đồng của tất cả sản phẩm đã like
    scores = np.mean(similarity_scores, axis=0)

    # Lấy chỉ số các sản phẩm tương tự nhất
    top_indices = scores.argsort()[-top_n:][::-1]

    top_indices = [i for i in top_indices if i < len(product_ids)]


    recommended_ids = [product_ids[i] for i in top_indices]


    # Lấy thông tin sản phẩm từ MongoDB
    recommendations = list(product_items.find({"_id": {"$in": recommended_ids}}))

    # Chuyển đổi ObjectId về string
    for r in recommendations:
        r["_id"] = str(r["_id"])
        if "product" in r and "_id" in r["product"]:
            r["product"]["_id"] = str(r["product"]["_id"])
        if "color" in r and "_id" in r["color"]:
            r["color"]["_id"] = str(r["color"]["_id"])

    print(f"[INFO] Trả về {len(recommendations)} sản phẩm được gợi ý cho user {user_id}")
    return recommendations