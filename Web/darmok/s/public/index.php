<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

require_once '../controller/MainController.php';

$mainController = new MainController();

    if (isset($POST['logout']))
    {

        setcookie("token", "", time() - 3600);
        $mainController->displayLogin();   
    }
    else if (isset($_COOKIE['token']))
    {
        $mainController->displayMap($_COOKIE['token']);
    }
    else
    {
        // log in
        if (isset($_POST['username'])) 
        {
            $mainController->login($_POST['username'], $_POST['password']);
        }
        // display insecure index
        else
        {
            $mainController->displayLogin();    
        }
    }

    
  
    
    



?>