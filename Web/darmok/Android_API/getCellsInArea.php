



<?php

include_once('dbConn.php');

$lat = isset($_POST['lat']) ? mysql_real_escape_string($_POST['lat']) : "";
$lng = isset($_POST['lng']) ? mysql_real_escape_string($_POST['lng']) : "";
//$lat = '44.810';
//$lng = '-63.601';

$json = array();

// Insert data into data base
$query = "SELECT * FROM Cell WHERE lat > (".$lat."-0.01) AND lat < (".$lat."+0.01) AND lng > (".$lng."-0.01) AND lng < (".$lng."+0.01)";
$result = mysql_query($query);
while ($row = mysql_fetch_assoc($result)) 
{
    $cell[] = $row;
}

$json['cell'] = $cell;

$query2 = "SELECT * FROM CellSupport ".
        "INNER JOIN Cell ON Cell.id = CellSupport.cell_id ".
        "WHERE lat > (".$lat."-0.01) AND lat < (".$lat."+0.01) AND lng > (".$lng."-0.01) AND lng < (".$lng."+0.01) ORDER BY CellSupport.amount DESC";
$result2 = mysql_query($query2);
while ($row2 = mysql_fetch_assoc($result2)) 
{
    $query3 = "SELECT * FROM Users WHERE id = " . $row2["user_id"];
    $result3 = mysql_query($query3);
    $row3 = mysql_fetch_assoc($result3);    
    
     $support[] = array( 
        'cell_id' => $row2["cell_id"],
        'username' => $row3["username"],
         'name' => $row3["name"],
         'color' => $row3["color"],
        'amount' => $row2["amount"],
        'lat' => $row2["lat"],
         'lng' => $row2["lng"]
    );
}
$json['support'] = $support;

//$struct = array("cell" => $cell);

@mysql_close($conn);

/* Output header */
header('Content-type: application/json');
echo json_encode($json);