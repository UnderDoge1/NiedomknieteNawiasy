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
    join_room("1")
    emit('status', {'msg': 'elko' + ' has entered the room.'}, room="1")
@socketio.on('text', namespace='/chat')
def text(message):
    room = session.get('room')
    emit('message', {'msg': message['msg'], 'senderId': message['senderId']}, room="1")


@socketio.on('left', namespace='/chat')
def left(message):
    room = session.get('room')
    username = session.get('username')
    leave_room(room)
    session.clear()
    emit('status', {'msg': username + ' has left the room.'}, room=room)


if __name__ == '__main__':
    socketio.run(app,'0.0.0.0', 5000, allow_unsafe_werkzeug=True)
