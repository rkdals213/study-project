export function getTickets() {
    return fetch('http://localhost:8080/tickets', {
        method: 'GET',
    })
}

export function purchaseTicket(ticketId, token) {
    return fetch('http://localhost:8080/tickets/purchase/' + ticketId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({token}), // name 값을 서버로 전송
    })
}

export function checkTicketAndLock(id) {
    return fetch('http://localhost:8080/tickets/checkTicketAndLock/' + id, {
        method: 'POST',
    })
}
