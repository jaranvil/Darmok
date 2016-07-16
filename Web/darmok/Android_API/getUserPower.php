



<?php

include_once('dbConn.php');

$username = isset($_POST['username']) ? mysql_real_escape_string($_POST['username']) : "";
//$username = "kineco";

//$lat = '44.810';
//$lng = '-63.601';

$json = array();
$power = 0;

$query = "SELECT id, level, xp FROM Users WHERE username = '".$username."';";
$result = mysql_query($query);
$row = mysql_fetch_assoc($result);
        
$id = $row['id'];    
$level = $row['level'];    
$xp = $row['xp'];
$query2 = "SELECT cell_id, amount FROM CellSupport WHERE user_id = ".$id;
$result2 = mysql_query($query2);

// for each cell this username has suppport in 
while ($row2 = mysql_fetch_assoc($result2)) 
{
  
    $query3 = "SELECT * FROM CellSupport WHERE cell_id = ".$row2['cell_id'];
    $result3 = mysql_query($query3);
    
    // find owner
    // TODO handle ties
    $owner_id = 0;
    $owner_amount = 0;
    while ($row3 = mysql_fetch_assoc($result3)) 
    {
        if ($row3['amount'] > $owner_amount)
        {
            $owner_amount = $row3['amount'];
            $owner_id = $row3['user_id'];
        }
    }
    
    if ($owner_id == $id)
    {
        $power++;
    }
}

echo $power.":".$level.":".$xp;



@mysql_close($conn);

/* Output header */
header('Content-type: application/json');
//echo json_encode($json);