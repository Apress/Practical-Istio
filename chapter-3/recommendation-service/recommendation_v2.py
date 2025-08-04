from flask import Flask, jsonify, request

app = Flask(__name__)

@app.route("/recommendations", methods=["GET", "POST"])
def handle_recommendations():
    if request.method == "GET":
        # Original GET logic
        recommendations = [
            {"id": 1, "title": "Movie X", "type": "AI-Enhanced"},
            {"id": 2, "title": "Movie Y", "type": "Personalized"},
            {"id": 3, "title": "Movie Z", "type": "Recently Watched"}
        ]
        return jsonify({"version": "v2", "recommendations": recommendations})
    
    elif request.method == "POST":
        # Get the JSON data from the request
        user_data = request.get_json()
        
        # Generate personalized recommendations based on user data
        # This is a simple example - you would typically use the user_data
        # to customize the recommendations
        recommendations = [
            {"id": 1, "title": f"Movie X for user {user_data.get('userId', 'unknown')}",
             "type": "AI-Enhanced"},
            {"id": 2, "title": "Movie Y", "type": "Personalized"},
            {"id": 3, "title": "Movie Z", "type": "Recently Watched"}
        ]
        
        return jsonify({
            "version": "v2",
            "recommendations": recommendations,
            "userPreferences": user_data
        })

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080)