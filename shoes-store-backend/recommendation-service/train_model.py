from dotenv import load_dotenv
load_dotenv()
import os

from pymongo import MongoClient
from sklearn.feature_extraction.text import TfidfVectorizer
import joblib

from utils.Util import build_text

MONGO_URI = os.getenv("MONGO_URI")
client = MongoClient(MONGO_URI)
db = client["shoes-store"]
product_items = list(db["product-item"].find({}))

documents = [build_text(item) for item in product_items if build_text(item)]
product_ids = [item["_id"] for item in product_items if build_text(item).strip()]

if not documents:
    raise ValueError("Không có dữ liệu hợp lệ để huấn luyện mô hình.")

vectorizer = TfidfVectorizer(stop_words="english")
tfidf_matrix = vectorizer.fit_transform(documents)

print(f"✅ Số sản phẩm hợp lệ: {len(product_ids)}")

joblib.dump({
    "tfidf_matrix": tfidf_matrix,
    "vectorizer": vectorizer,
    "product_ids": product_ids
}, "model.pkl")

print("✅ Đã huấn luyện và lưu mô hình vào 'model.pkl'")

