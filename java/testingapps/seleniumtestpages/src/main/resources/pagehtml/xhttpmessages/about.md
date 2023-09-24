# About XHTTP Messages

<div class="explanation">
        <p>This page makes requests to a server and displays the messages received.
        </p>
</div>

<!-- TOC -->

## Page Specific Details

- The number of requests sent is randomized.
- The first number after "Message Count" is the number of requests to send.
- The second number after "Message Count" is the number of messages to display.  
- The messages received are random.
- The processing is complete when `Message Count: 0 : 0` is displayed.

## Automating Visually

You can synchronize your automated execution by monitoring the request and message count.

`Message Count: <number of requests to send> : <number of messages to render>`

You can also count the number of messages in the list to make sure they are the total you expect.

The display for the Message Count updates every second so there may be a delay between finishing the message processing and representing this on screen.

## Internal Variables

The page has a number of internal variables that are available to check when automating.

- You could make sure these match the numbers represented on screen.
- You could use these completely for synchronization.

The variables are:

- `liveMessages`,
- `totalRequestsMade`,
- `renderingQueueCount`,
- `totalMessagesReceived`.
  
The application is 'finished' when all requests have been sent and the `renderingQueueCount` equals 0.
