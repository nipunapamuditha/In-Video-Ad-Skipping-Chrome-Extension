document.addEventListener('yt-navigate-finish', function() {
    console.log('yt-navigate-finish event detected.');

    setTimeout(() => {
        console.log('Timeout completed, calling clickShowTranscriptButton.');
        clickShowTranscriptButton();
    }, 3000);
});

function clickShowTranscriptButton() {
    console.log('clickShowTranscriptButton function called.');
    const buttons = document.querySelectorAll('button');
    for (let button of buttons) {
        if (button.innerText.includes('Show transcript')) {
            console.log('Show transcript button found and clicked.');
            button.click();
            setTimeout(() => {
                console.log('Timeout completed, calling fetchTranscript.');
                fetchTranscript();
            }, 3000); // Wait for the transcript to load
            break;
        }
    }
}

function fetchTranscript() {
    console.log('fetchTranscript function called.');
    const transcriptElements = document.querySelectorAll('ytd-transcript-segment-renderer');
    let transcript = '';
    transcriptElements.forEach(element => {
        transcript += element.innerText + ' ';
    });

    transcript = transcript.replace(/\n/g, ' ').replace(/\s{2,}/g, ' ');
    console.log('Transcript fetched and processed');

    const video = document.querySelector('video');
    if (video) {
        console.log('Video element found.');
        // Extract video ID from URL
        const urlParams = new URLSearchParams(window.location.search);
        const videoId = urlParams.get('v');
        console.log('Video ID extracted:', videoId);

        // Get video duration in seconds
        const videoDuration = video.duration;
        const videoDurationMinutes = videoDuration / 60;
        console.log('Video duration in minutes:', videoDurationMinutes);

        if (videoDurationMinutes > 30) {
            transcript = "emptyString";
        }

        // Check if video duration is less than 30 minutes
        if (videoDurationMinutes < 300) {
            console.log('Video duration is less than 30 minutes, calling API with transcript.');
            fetch('https://newsloop.xyz/getadtimestamps', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ videoid: transcript, transcript: videoId })
            })
            .then(response => response.json())
            .then(data => {
                console.log('API response received:', data);

                // Store the time ranges from the API response
                const timeRanges = data;
                let totalSkippedTime = parseFloat(localStorage.getItem('totalSkippedTime')) || 0;

                video.addEventListener('timeupdate', function() {
                    for (let i = 0; i < timeRanges.length; i++) {
                        const [start, end] = timeRanges[i];
                        if (video.currentTime >= start && video.currentTime < end) {
                            totalSkippedTime += (end - start);
                            video.currentTime = end;
                            console.log(`Skipped from ${start} to ${end}. Total skipped time: ${totalSkippedTime}`);
                            break; // Exit the loop once a match is found
                        }
                    }
                    // Store the updated total skipped time in localStorage
                    localStorage.setItem('totalSkippedTime', totalSkippedTime);
                });
                console.log('timeupdate event listener added to video.');
            })
            .catch(error => {
                console.error('Error fetching API:', error);
            });
        } else {
            console.log('Video duration is 30 minutes or more, API call skipped.');
        }
    }
}