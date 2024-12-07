from flask import Flask, render_template, request, jsonify
from models import SpaceCraft
from config import db, SQLALCHEMY_DATABASE_URI

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = SQLALCHEMY_DATABASE_URI

db.init_app(app)

with app.app_context():
    db.create_all()

# EXAMPLE BASIC HTMLVIEW
@app.route('/')
def index():
    spacecrafts = SpaceCraft.query.all()
    return render_template('index.html', spacecrafts=spacecrafts)


@app.route('/register', methods=['POST'])
def register_spaceship():

    call_sign = request.form['call_sign']
    pos_x = request.form['pos_x']
    pos_y = request.form['pos_y']
    vel_x = request.form['vel_x']
    vel_y = request.form['vel_y']

    spaceship = SpaceCraft(
        call_sign=call_sign,
        pos_x=pos_x,
        pos_y=pos_y,
        vel_x=vel_x,
        vel_y=vel_y
    )

    try:

        db.session.add(spaceship)
        db.session.commit()
        return jsonify({'message': f'Spaceship {call_sign} added successfully'}), 201
    
    except Exception as e:

        db.session.rollback()
        return jsonify({'error': str(e)}), 400


@app.route('/register', methods=['GET'])
def get_spaceships_register():
    
    spacecrafts = SpaceCraft.query.all()
    return jsonify([spacecraft.to_dict() for spacecraft in spacecrafts]), 200



if __name__ == '__main__':

    app.run(debug=True)

