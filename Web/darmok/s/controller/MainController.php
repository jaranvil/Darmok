<?php

require_once('../model/UserModel.php');

class MainController 
{
    public $userModel;
    
    public function __construct()
    {
        $this->userModel = new UserModel();
    }
    
    public function displayMap($token)
    {
        $user = $this->userModel->getUser($token);
        $location = $this->userModel->getLocation($token);
        include '../view/map.php';
    }
    
    public function displayLogin()
    {
        include '../view/login.php';
    }
    
    public function login($username, $password)
    {
        $token = $this->userModel->getToken($username, $password);
        
        if ($token == 0)        
        {
            $error = "Username and password not found.";
            include '../view/login.php';
        }
        else
        {
            setcookie("token", $token, time() + (86400 * 30), "/");
            $this->displayMap($token);
        }
    }
    

    
    


}

?>
