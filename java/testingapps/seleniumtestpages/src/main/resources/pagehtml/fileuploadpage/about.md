# About File Upload Page 

<div class="explanation">
        <p>This page allows you to upload a file. Note: this will not save the file to the server. And there may be a file upload size limit.
        </p>
</div>

<!-- TOC -->

## Safe Upload File

This upload process goes through the full uploading process but does not save your file to the server. This means you are safe to use any file.

_Be careful with all Test Practice Applications on the internet because some of them do save your file, and make it available to anyone to download._

**Never use private or confidential material or any files that you would not want to make public.**

## Functionality

Choose the file from your local drive.

Choose the type of file.

Click "Upload".

The file length will be checked and when the 'upload' is successful you will be redirected to the "Uploaded File" page.

## Automating

Because the File Upload Dialog is a native dialog to the browser, it is not something we would want to automate. Prior to tool support we did have to automate the native dialog.

Most automated execution tools now have special approaches for amending the file upload field.

In WebDriver `find` the `input` element and use `sendKeys()` to type in the absolute path of a local file.


## Saving The File

If you run the test pages app locally then you can save files that are uploaded.

Additionally if you are running the application locally, when you upload an image, the image will be displayed on the `Uploaded File` page.