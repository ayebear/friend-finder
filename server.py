import json
from flask import Flask
from flask import request, abort, redirect, url_for
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
    # Returns 'Yes' when the specified user is logged in
    rq = getRequest(request)
    username = rq.get('username')
    if username in clients:
        return 'Yes'
    return 'No'

@app.route('/login', methods=['GET', 'POST'])
def login():
    # Creates user on server to store location
    rq = getRequest(request)
    username = rq.get('username')

    if username and username not in clients:
        client = {}
        client['username'] = username
        # client['position'] = 'GPS location'
        clients[username] = client
        return username

    return ''

@app.route('/logout', methods=['GET', 'POST'])
def logout():
    # Removes user data from server
    rq = getRequest(request)
    username = rq.get('username')

    if username in clients:
        clients.pop(username)

    return ''

@app.route('/update', methods=['GET', 'POST'])
def update():
    # Updates client coordinates
    rq = getRequest(request)
    username = rq.get('username')
    if username and username in clients:
        fields = ['latitude', 'longitude', 'direction']

        for fieldName in fields:
            field = rq.get(fieldName)
            if field:
                clients[username][fieldName] = field

        return json.dumps(clients[username])

    # abort(404)
    return ''

@app.route('/getallclients')
def getallclients():
    # Returns JSON string of all client data
    return json.dumps(clients)

@app.route('/getclient', methods=['GET', 'POST'])
def getclient():
    # Returns JSON string of the specified client
    rq = getRequest(request)
    username = rq.get('username')

    if username in clients:
        return json.dumps(clients[username])

    return ''

if __name__ == "__main__":
    # app.run()
    app.run(debug=True, host='0.0.0.0')
