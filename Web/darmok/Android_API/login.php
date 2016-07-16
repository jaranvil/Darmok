



<?php

include_once('dbConn.php');

$username = isset($_POST['username']) ? mysql_real_escape_string($_POST['username']) : "";
$password = isset($_POST['password']) ? mysql_real_escape_string($_POST['password']) : "";

//// Insert data into data base
$query = "SELECT salt FROM Users WHERE username = '".$username."';";
$result = mysql_query($query);

$salt = "";
while ($row = mysql_fetch_assoc($result)) {
    $salt = $row["salt"];
}

$pass = hash('sha256', $password . $salt);

$query2 = "SELECT * FROM Users WHERE username = '".$username."' AND hash = '".$pass."';";
$result2 = mysql_query($query2);

$user_id = 0;
while ($row = mysql_fetch_assoc($result2)) {
    $user_id = $row["user_id"];
}

echo $user_id;
//echo $pass;
//echo $username . $password . $salt . $pass;


@mysql_close($conn);

