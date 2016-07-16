



<?php

include_once('dbConn.php');

$lat = isset($_POST['lat']) ? mysql_real_escape_string($_POST['lat']) : "";
$lng = isset($_POST['lng']) ? mysql_real_escape_string($_POST['lng']) : "";
$user_id = isset($_POST['user_id']) ? mysql_real_escape_string($_POST['user_id']) : "";
$amount = isset($_POST['amount']) ? mysql_real_escape_string($_POST['amount']) : "";
//$lat = '44.810';
//$lng = '-63.600';
//$user_id = 657567;
//$amount = 2;

$json = array();


// Select Cell
$query = "SELECT * FROM Cell WHERE lat = ".$lat." AND lng = ".$lng.";";
$result = mysql_query($query);
$cell_id = 0;
while ($row = mysql_fetch_assoc($result)) {
    $cell_id = $row["id"];
}



// Create Cell is none found
if ($cell_id == 0)
{
    $insert_query = "INSERT INTO Cell (lat, lng) values (".$lat.", ".$lng.");";
    $insert_result = mysql_query($insert_query);
    
    $id_query = "SELECT id FROM Cell WHERE lat = ".$lat." AND lng = ".$lng.";";
    $id_result = mysql_query($id_query);
    while ($new_row = mysql_fetch_assoc($id_result)) {
        $cell_id = $new_row["id"];
    }
}


// Get user ID from the token posted in
$query2 = "SELECT id FROM Users WHERE user_id = ".$user_id.";";
$result2 = mysql_query($query2);

$id = 0;
while ($row2 = mysql_fetch_assoc($result2)) {
    $id = $row2["id"];
}

echo $cell_id;
echo $id;
// Add Support
if ($cell_id != 0 && $id != 0)
{
    //$json['cell'] = $cell;

    // if record exsists, update it
    $query3 = "SELECT * FROM CellSupport WHERE user_id = ".$id." AND cell_id = ".$cell_id.";";
    $result3 = mysql_query($query3);
    $count = mysql_num_rows($result3);
    if ($count >0)
    {
        $query4 = "UPDATE CellSupport SET amount = amount + ".$amount." WHERE cell_id = ".$cell_id." AND user_id = ".$id.";";        
    }
    else
    {
        $query4 = "INSERT INTO CellSupport (user_id, cell_id, amount) VALUES (".$id.", ".$cell_id.", ".$amount.")";
    }
    
    echo $query4;
    
    $result4 = mysql_query($query4);
}



//$struct = array("cell" => $cell);

@mysql_close($conn);

/* Output header */
	header('Content-type: application/json');
	echo json_encode($json);