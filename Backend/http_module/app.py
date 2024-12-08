from flask import Flask, render_template, request, jsonify
from models import SpaceCraft
from config import db, SQLALCHEMY_DATABASE_URI




if __name__ == '__main__':

    app.run(debug=True)

