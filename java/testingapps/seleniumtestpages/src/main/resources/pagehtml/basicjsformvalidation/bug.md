This page has at least 2 bugs.

The value is auto converted to a string when parseInt
is used and if it contains scientific notation then it will be ignored
e.g. 23e3 is 23000 but entering this will convert it to 23 and this will
pass the validation.

The number field does not save us from this issue.

Also the check is >30 rather than <30