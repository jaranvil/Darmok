<?php

require_once('../model/AuthModel.php');

class MainController 
{
    public $auth;
    
    public function __construct()
    {
        $this->auth = new AuthModel();
    }
    
    public function displayMap($token)
    {
            $user = $this->auth->getUser($token);
            include '../view/map.php';
    }
    
    public function displayLogin()
    {
        include '../view/login.php';
    }
    
    public function login($username, $password)
    {
        $token = $this->auth->getToken($username, $password);
        
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
