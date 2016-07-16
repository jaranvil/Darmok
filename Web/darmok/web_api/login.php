

<?php

include_once('dbConn.php');

$username = isset($_POST['username']) ? mysql_real_escape_string($_POST['username']) : "";
$password = isset($_POST['password']) ? mysql_real_escape_string($_POST['password']) : "";
//$lat = '44.810';
//$lng = '-63.601';

$json = array();


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

$json['token'] = $user_id;



//$struct = array("cell" => $cell);

@mysql_close($conn);

/* Output header */
header('Content-type: application/json');
echo json_encode($json);