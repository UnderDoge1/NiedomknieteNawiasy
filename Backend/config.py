from flask_sqlalchemy import SQLAlchemy

SQLALCHEMY_DATABASE_URI = 'sqlite:///sqlite_db.db'

db = SQLAlchemy()