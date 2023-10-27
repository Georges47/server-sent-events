onMessage = (event) => {
    const data = JSON.parse(event.data);
    const progressValue = data.value;
    document.getElementById('progress-bar').style.width = progressValue + '%';
    document.getElementById('progress-value').textContent = progressValue + '%';
    if (progressValue === 100) {
        document.getElementById('progress-message').textContent = 'Loaded successfully!'
        document.getElementById('restart-button').style.display = 'block';
        eventSource.close();
    }
};

onError = (error) => {
    console.error('SSE Error: ' + error);
    eventSource.close();
};

let eventSource = new EventSource('http://localhost:8080/sse');
eventSource.onmessage = onMessage;
eventSource.onerror = onError;

const restartButton = document.getElementById('restart-button');
restartButton.addEventListener('click', () => {
    restartButton.style.display = 'none';
    document.getElementById('progress-bar').style.width = '0%';
    document.getElementById('progress-message').textContent = 'Loading...'
    eventSource = new EventSource('http://localhost:8080/sse');
    eventSource.onmessage = onMessage;
    eventSource.onerror = onError;
});
