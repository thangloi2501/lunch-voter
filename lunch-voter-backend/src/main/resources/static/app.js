const stompClient = new StompJs.Client({
    brokerURL: 'http://localhost:8080/ws'
});

stompClient.onConnect = (frame) => {
    code = localStorage.getItem('code');
    stompClient.subscribe('/topic/vote/' + code, (content) => {
        showGreeting(JSON.parse(content.body).content);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function create() {
  const url = 'http://localhost:8080/api/v1/votes';
  const data = {
    'name': $("#name").val()
  };

  fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        // Add any other headers if needed
      },
      body: JSON.stringify(data),
    })
    .then(response => {
      if (response.ok) {
            return response.json(); // Parse the JSON response
          } else {
              return response.json().then(error => {
                    throw new Error(`${error.message}`);
                  });
          }
    })
    .then(res => {
      $("#code").val(res.data.code);
      localStorage.setItem('code', res.data.code);
      localStorage.setItem('userCode', res.data.userCode);
      localStorage.setItem('name', $("#name").val());

      $("#create").prop("disabled", true);
      $("#end").prop("disabled", false);

      stompClient.activate();
    })
    .catch(error => {
      console.log(error);
      showGreeting(error.message);
    });
}

function join() {
  const url = 'http://localhost:8080/api/v1/votes/join';
  const data = {
    'name': $("#name").val(),
    'code': $("#code").val()
  };

  fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        // Add any other headers if needed
      },
      body: JSON.stringify(data),
    })
    .then(response => {
      if (response.ok) {
            return response.json(); // Parse the JSON response
          } else {
              return response.json().then(error => {
                    throw new Error(`${error.message}`);
                  });
          }
    })
    .then(res => {
      disconnect();
      localStorage.setItem('code', res.data.code);
      localStorage.setItem('userCode', res.data.userCode);

      stompClient.activate();
    })
    .catch(error => {
      console.log(error);
      showGreeting(error.message);
    });
}

function disconnect() {
    stompClient.deactivate();
    console.log("Disconnected");
}

function submit() {
  const url = 'http://localhost:8080/api/v1/votes/submit';
  const data = {
     'code': localStorage.getItem('code'),
     'userCode': localStorage.getItem('userCode'),
     'voteValue': $("#vote").val()
  };

  fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        // Add any other headers if needed
      },
      body: JSON.stringify(data),
    })
    .then(response => {
      if (response.ok) {
            return response.json(); // Parse the JSON response
          } else {
              return response.json().then(error => {
                    throw new Error(`${error.message}`);
                  });
          }
    })
    .then(res => {
      //nothing

    })
    .catch(error => {
      console.log(error);
      showGreeting(error.message);
    });

    //todo: OpenAPI document rest + websocket
    //todo: with SockJS
    //todo: unit tests
}

function endSession() {
const url = 'http://localhost:8080/api/v1/votes';
  const data = {
                         'code': localStorage.getItem('code'),
                         'userCode': localStorage.getItem('userCode')
                       };

  fetch(url, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        // Add any other headers if needed
      },
      body: JSON.stringify(data),
    })
    .then(response => {
      if (response.ok) {
            return response.json(); // Parse the JSON response
          } else {
              return response.json().then(error => {
                    throw new Error(`${error.message}`);
                  });
          }
    })
    .then(res => {
      $("#create").prop("disabled", false);
            $("#end").prop("disabled", true);

    })
    .catch(error => {
      console.log(error);
      showGreeting(error.message);
    });
}

function showGreeting(message) {
    $("#contents").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#create" ).click(() => create());
    $( "#end" ).click(() => endSession());
    $( "#send" ).click(() => submit());
    $( "#join" ).click(() => join());
});