export function getEntranceToken(accountId) {
    return fetch('http://localhost:8080/reservation', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({accountId}), // name 값을 서버로 전송
    })
}


export function connectToOrderSubscriber(token) {
    return new EventSource('http://localhost:8080/reservation/subscribe/entrance?token=' + token);
}
