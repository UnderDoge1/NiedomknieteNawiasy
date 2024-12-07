from Backend.http_module.config import db
import datetime


class SpaceCraft(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    call_sign = db.Column(db.String(80), unique=True, nullable=False)
    pos_x = db.Column(db.Double)
    pos_y = db.Column(db.Double)
    vel_x = db.Column(db.Double)
    vel_y = db.Column(db.Double)
    timestamp = db.Column(db.DateTime, default=datetime.datetime.now)

    def __repr__(self):
        return '<SpaceCraft %r>' % self.call_sign
