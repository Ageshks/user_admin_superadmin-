async function sendMessage(ticketId) {
    const messageContent = document.getElementById('messageContent').value;
    const senderId = 1;  // Change to actual sender ID
    const messageType = 'USER_MESSAGE'; // You can dynamically assign message type

    const response = await fetch('/support-chat/message', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({
            ticketId: ticketId,
            senderId: senderId,
            messageContent: messageContent,
            messageType: messageType
        })
    });

    const message = await response.json();
    console.log('Message sent:', message);
}
