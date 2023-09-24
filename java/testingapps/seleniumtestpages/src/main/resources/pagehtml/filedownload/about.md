# About File Download Page 

<div class="explanation">
        <p>This page implements multiple ways initiating a file download.
        </p>
</div>

<!-- TOC -->

## Download Files

File Downloads are relatively easy to test.

- Click Download
- Check Results

They are sometimes a little harder to automate.

## Automating Download Files

One approach to automating Downloads is to use an HTTP library and directly pull down the file.

The other is to use the default functionality in your chosen Browser Automating Tool and trap the file download.

## Page Functionality

The page has multiple approaches to generating the file.

No JavaScript is used so each link or button has a reference to the file URL.

Some of these connect directly to a file on the server, some make a server call which generates and returns the file.

## Automating

Try and trigger each download mechanism using both direct and browser based automating.

Make sure to compare the results of the download to what you expected.
