<?php



class UserDataModel
{

    
    
    private $dbConnection;
    private $result;

    public function connectToDB()
    {
        include('dbConn.php');
        $this->dbConnection = $dbConn;
        
         if (!$this->dbConnection)
         {
               die('Could not connect to the Database: ' .
                        $this->dbConnection->connect_errno);
         }
    }

    public function closeDB()
    {  
        $this->dbConnection->close();
    }

    function dbEsc($string)
    {
        return $this->dbConnection->real_escape_string(htmlspecialchars($string));
    }
    
    public function getToken($username, $password)
    {
       $salt = '';
       $query = "select salt from Users where username = '".$this->dbEsc($username). "';";
       
       $result = @$this->dbConnection->query($query);
        $row = $result->fetch_array();
    
        $salt = $row['salt'];
                
        $hash = hash("sha256", $password.$salt);

        //die("test" . " ". $username . " " . $hash . " " . $salt);
        
	   $query = "select user_id from Users where username = '" . $this->dbEsc($username) . "' AND hash = '" . $hash . "';";
        $result = @$this->dbConnection->query($query);
        
        $row = $result->fetch_array();
        $token = $row['user_id'];
                
        return $token;

    }
    
    public function getUser($token)
    {
        $query = "SELECT * FROM Users WHERE user_id = '".$this->dbEsc($token)."'";
        $result = @$this->dbConnection->query($query);
        return $result->fetch_array();        
    }
}

?>
