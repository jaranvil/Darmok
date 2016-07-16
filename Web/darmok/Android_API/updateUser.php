



<?php

include_once('dbConn.php');

$user_id = isset($_POST['user_id']) ? mysql_real_escape_string($_POST['user_id']) : "";
$amount = isset($_POST['xp']) ? mysql_real_escape_string($_POST['xp']) : "";
$xpMax = isset($_POST['xpMax']) ? mysql_real_escape_string($_POST['xpMax']) : "";
//$user_id = 37578;
//$amount = 10;
//$xpMax = 100;

// get current XP
$query = "SELECT xp FROM Users WHERE user_id = ".$user_id.";";  
$result = mysql_query($query);
$row = mysql_fetch_assoc($result);

$xp = $row['xp'];

echo "xp ".($xp + $amount) . " | ";
echo "max ".$xpMax . " | ";
if ($xp + $amount >= $xpMax)
{
    // new level 
    echo "Level Up";
    $query = "UPDATE Users SET level = level + 1, xp = 0 WHERE user_id = ".$user_id.";";  
}
else {
    $sum = $xp + $amount;
    $query = "UPDATE Users SET xp = ".$sum." WHERE user_id = ".$user_id.";";   
}

$result = mysql_query($query);



@mysql_close($conn);

