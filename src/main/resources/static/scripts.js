var stompClient = null;

$(document).ready(function () {
    console.log("Index page is ready");
    $("#send-private").click(function () {
        sendPrivateMessage();
    });
    $("#send-broadcast").click(function () {
        sendBroadcastMessage();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#generate").click(function () {
        generateConnections();
    });
});

function connect() {
    let apiKey = document.getElementById("key-id").value;
    let wsId = document.getElementById("ws-id").value;

    // Try to set up WebSocket connection with the handshake
    var socket = new SockJS('/ws-register?authKey=' + apiKey);
    // Create a new StompClient object with the WebSocket endpoint
    stompClient = Stomp.over(socket);

    // Start the STOMP communications, provide a callback for when the CONNECT frame arrives.
    stompClient.connect(
        {'authKey': apiKey, 'ws-id': wsId},
        function (frame) {
            showInfo(frame);
            console.log(frame);
            stompClient.subscribe('/user/queue/messages', function (message) {
                showMessage(JSON.parse(message.body).content);
            });
        },
        function (err) {
            showInfo('Error happened: ' + err);
        }
    );

    // Try to set up WebSocket connection with the handshake
    var socket2 = new SockJS('/ws-register?authKey=' + apiKey);
    // Create a new StompClient object with the WebSocket endpoint
    var stompClient2 = Stomp.over(socket2);

    stompClient2.connect(
        {'authKey': apiKey, 'ws-id': wsId},
        function (frame) {
            showInfo(frame);
            console.log(frame);
            stompClient2.subscribe('/topic/messages', function (message) {
                showMessage(JSON.parse(message.body).content);
            });
        },
        function (err) {
            showInfo('Error happened: ' + err);
        }
    );

}

function generateConnections() {
    let from = document.getElementById("number-from").value;
    let to = document.getElementById("number-to").value;
    // Try to set up WebSocket connection with the handshake

    for (let i = from; i < to; i++) {
        // Create a new StompClient object with the WebSocket endpoint
        let socket = new SockJS('/ws-register?authKey=k');
        let stompClient = Stomp.over(socket);

        // Start the STOMP communications, provide a callback for when the CONNECT frame arrives.
        stompClient.connect(
            {'authKey': 'k', 'ws-id': ('ws' + i)},
            function (frame) {
                showInfo(frame);
                console.log(frame);
                stompClient.subscribe('/user/queue/messages', function (message) {
                    showMessage(JSON.parse(message.body).content);
                });
            },
            function (err) {
                showInfo('Error happened: ' + err);
            }
        );
        sleep(1000);
    }
}

function sendPrivateMessage() {
    let message = document.getElementById("private-message").value;
    let userId = document.getElementById("user-id").value;
    let apiKey = document.getElementById("key-id").value;
    console.log("Sending private message '" + message + "' to user " + userId);

    stompClient.send(
        "/app/message/" + userId,
        {'authKey': apiKey},
        JSON.stringify({'content': message})
    );
}

function sendBroadcastMessage() {
    let message = document.getElementById("private-broadcast-message").value;
    let apiKey = document.getElementById("key-id").value;
    console.log("Sending broadcast message '" + message);

    stompClient.send(
        "/app/message",
        {'authKey': apiKey},
        JSON.stringify({'content': message})
    );
}

function sleep(ms) {
    let start = new Date().getTime(), expire = start + ms;
    while (new Date().getTime() < expire) {
    }
}

function showMessage(message) {
    $("#messages").append("<tr><td style='font-size: 8px'>" + message + "</td></tr>");
}

function showInfo(message) {
    $("#connected").append("<tr><td>" + message + "</td></tr>");
}
