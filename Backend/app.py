from flask import Flask, jsonify
app = Flask(__name__)

@app.route('/')
def start():
    return 'sranie -- endpoint post,get ----> /register'
@app.route('/resiter', methods=['POST'])
def register_spaceship():
    return jsonify({'message': 'sranie'}), 501

@app.route('/register', methods=['GET'])
def get_spaceships_register():
    return 'niezesrajsie'



if __name__ == '__main__':
    app.run(debug=True)
