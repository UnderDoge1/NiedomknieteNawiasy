from flask import Flask, render_template, url_for, request, redirect, session
from flask_socketio import SocketIO, join_room, leave_room, emit
from flask_session import Session

# FLASK_APP = app.py
# FLASK_ENV = development
# FLASK_DEBUG = 0

app = Flask(__name__)
app.debug = True
app.config['SECRET_KEY'] = 'secret_key'
app.config['SESSION_TYPE'] = 'filesystem'

Session(app)

socketio = SocketIO(app, manage_session=False)


@app.route('/', methods=['GET', 'POST'])
def index():
    return render_template('index.html')


@app.route('/chat', methods=['GET', 'POST'])
def chat():
    if request.method == 'POST':
        username = request.form['username']
        room = request.form['room']
        session['username'] = username
        session['room'] = room
        return render_template('chat.html', session=session)
    else:
        if session.get('username') is not None:
            return render_template('chat.html', session=session)
        else:
            return redirect(url_for('index'))


@socketio.on('join', namespace='/chat')
def join():
    room = session.get('room')
    join_room(room)
    emit('status', {'msg': session.get('username') + ' has entered the room.'}, room=room)


@socketio.on('text', namespace='/chat')
def text(message):
    room = session.get('room')
    emit('message', {'msg': session.get('username') + ' : ' + message['msg']}, room=room)


@socketio.on('left', namespace='/chat')
def left(message):
    room = session.get('room')
    username = session.get('username')
    leave_room(room)
    session.clear()
    emit('status', {'msg': username + ' has left the room.'}, room=room)

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




    #----------------------------------------------HTTP SERVER----------------------------------------------
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
    socketio.run(app,'0.0.0.0', 5000, allow_unsafe_werkzeug=True)
