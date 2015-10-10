import json
from flask import Flask
from flask import request
app = Flask(__name__)

clients = {}

def getRequest(request):
    rq = None
    if request.method == 'POST':
        rq = request.form
    else:
        rq = request.args
    return rq

@app.route('/')
def index():
    return 'Index Page'

@app.route('/amiloggedin', methods=['GET', 'POST'])
def hello():
    rq = getRequest(request)
    username = rq.get('username')
    if username in clients:
        return 'Yes'
    return 'No'

@app.route('/login', methods=['GET', 'POST'])
def login():
    rq = getRequest(request)
    username = rq.get('username')

    if username and username not in clients:
        client = {}
        client['username'] = username
        client['position'] = 'GPS location'
        clients[username] = client
        return username
    return ''

@app.route('/logout', methods=['GET', 'POST'])
def logout():
    rq = getRequest(request)
    username = rq.get('username')

    if username in clients:
        clients.pop(username)
    # del clients[username]

    return ''

# Login(username)
# Logout(username)
# Update(GpsCoordinates, Bluetooth, etc.)
# GetAllClients()
# GetClient(username)


if __name__ == "__main__":
    # app.run()
    app.run(debug=True)
