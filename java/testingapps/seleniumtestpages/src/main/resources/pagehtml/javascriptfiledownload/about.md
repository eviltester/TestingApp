# About JavaScript File Download Page 

<div class="explanation">
        <p>This page implements multiple ways of triggering a file download.
        </p>
</div>

## Download Files

File Downloads are relatively easy to test.

- Click Download
- Check Results

They are sometimes a little harder to automate.

## Automating Download Files

One approach to automating Downloads is to use an HTTP library and directly pull down the file.

This is slightly harder to do when the file download initiates from JavaScript.

## Functionality

The page has multiple approaches to generating the file.

The common theme is that none of the buttons or links connects to the file directly. They have to be clicked, then the file download is initiated.

Any Automated Execution has to trap or handle the background file download.

## Automating

Try and trigger each download mechanism using browser based automating.

Make sure to compare the results of the download to what you expected.
