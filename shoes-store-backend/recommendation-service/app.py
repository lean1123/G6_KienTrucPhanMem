from dotenv import load_dotenv
load_dotenv()
import os
from flask import Flask, request, jsonify
from recommender import get_recommendations
import py_eureka_client.eureka_client as eureka_client

app = Flask(__name__)

# Đăng ký Eureka
eureka_client.init(
    eureka_server= os.getenv("EUREKA_URI"),
    app_name="recommendation-service",
    instance_port=5001,
    instance_host="localhost",
)

@app.route('/recommend', methods=['GET'])
def recommend():
    user_id = request.args.get("userId")
    if not user_id:
        return jsonify({"error": "Missing userId"}), 400

    try:
        result = get_recommendations(user_id)
        converted = [convert_mongo_id(item) for item in result]
        return jsonify(converted), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# Hàm chuyển _id → id (bao gồm nested fields)
def convert_mongo_id(doc):
    if isinstance(doc, dict):
        new_doc = {}
        for key, value in doc.items():
            if key == "_id":
                new_doc["id"] = str(value)
            elif key == "_class":
                continue
            else:
                new_doc[key] = convert_mongo_id(value)
        return new_doc
    elif isinstance(doc, list):
        return [convert_mongo_id(item) for item in doc]
    else:
        return doc

if __name__ == "__main__":
    app.run(port=5000)
