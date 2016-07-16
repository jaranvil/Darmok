



<?php

include_once('dbConn.php');

$user_id = isset($_POST['user_id']) ? mysql_real_escape_string($_POST['user_id']) : "";
$name = isset($_POST['name']) ? mysql_real_escape_string($_POST['name']) : "";
$color = isset($_POST['color']) ? mysql_real_escape_string($_POST['color']) : "";

$query = "UPDATE Users SET name = '".$name."', color = '".$color."' WHERE user_id = ".$user_id.";";   
$result = mysql_query($query);



@mysql_close($conn);

