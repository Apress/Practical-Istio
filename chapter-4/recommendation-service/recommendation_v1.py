from flask import Flask, jsonify

app = Flask(__name__)

@app.route("/recommendations", methods=["GET"])
def get_recommendations():
    recommendations = [
        {"id": 1, "title": "Movie A", "type": "Popular"},
        {"id": 2, "title": "Movie B", "type": "Trending"},
        {"id": 3, "title": "Movie C", "type": "Top Rated"}
    ]
    return jsonify({"version": "v1", "recommendations": recommendations})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080)