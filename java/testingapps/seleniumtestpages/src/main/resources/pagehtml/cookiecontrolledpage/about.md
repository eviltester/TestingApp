# About Cookie Controlled Page

<div class="explanation">
        <p>A 'fake' login page using JavaScript and Cookies.
        </p>
</div>

## Functionality

This page simulates a login process.

You should not be able to visit the AdminView or SuperAdminView
pages without being 'logged in' as the correct the user

You should only be able to visit the SuperAdminView
page when logged in as `SuperAdmin`.

And (once logged in) you should not be able to come back to this page until you logout.

Try it and see - and use dev tools to mess with the cookie or try and inject a cookie with your automated execution

When you type in the correct username (e.g. Admin) and password (AdminPass), a cookie is created, and you are redirected to the associated 'Admin View' page.

Users:

- `Admin` / `AdminPass`
- `SuperAdmin` / `AdminPass`

The Cookie represents a session cookie that would be created upon successful login for a normal login process.

Each page has JavaScript that detects the cookie.

You can only access the [AdminView page](/styled/cookies/adminview.html) or [SuperAdminView page](/styled/cookies/superadminview.html) when the cookie is set.

When the cookie is set the [login page](/styled/cookies/adminlogin.html) will redirect you to the AdminView Page.

Use the `AdminLogout` link to remove the cookie.

## Cookies

Using your Automated Execution tool you should be able to create a cookie and bypass the login process.

Injecting cookies is often done during Automated Execution to speed up access to a logged in section of an application.

## Cookie Inspection and Manipulation

Use the Browser Dev Tools to inspect the cookie and find the correct format and information to add.

You can also change the cookie details and delete them using the Browser Dev Tools to help you with your testing.


