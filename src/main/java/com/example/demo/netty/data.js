var ws = new WebSocket("wss://localhost:8080/ws");

ws.onopen = function() {
    console.log("Connected to server");
    ws.send("Hello Server");
    startHeartbeat();
};

ws.onmessage = function(event) {
    var messages = document.getElementById("messages");
    var message = document.createElement("div");
    message.textContent = "Received: " + event.data;
    messages.appendChild(message);
    resetHeartbeat();
};

ws.onclose = function() {
    console.log("Disconnected from server");
};

ws.onerror = function(error) {
    console.log("WebSocket Error: " + error);
};

var heartbeatInterval;
function startHeartbeat() {
    heartbeatInterval = setInterval(function() {
        ws.send("ping");
    }, 30000); // 每30秒发送一次心跳
}

function resetHeartbeat() {
    clearInterval(heartbeatInterval);
    startHeartbeat();
}
