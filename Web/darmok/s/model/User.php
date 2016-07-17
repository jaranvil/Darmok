<?php

require_once('../model/Address.php');

class User
{
    private $username;
    private $email;
    private $name;
    private $color;
    private $level;
    private $xp;
    
    
    public function __construct($username, $email, $name, $color, $level, $xp)
    {
        $this->username = $username;
        $this->email = $email;
        $this->name = $name;
        $this->color = $color;
        $this->level = $level;
        $this->xp = $xp;
    }
    
    public function getUsername()
    {
        return ($this->username);
    }
    
    public function getEmail()
    {
        return ($this->email);
    }
    
    public function setUsername($username)
    {
        $this->username = $username;
    }

    public function setEmail()
    {
        return ($this->email);
    }
    
    public function setName($name)
    {
        $this->name = $name;
    }
    
    public function getName()
    {
        return ($this->name);
    }
    
    public function setColor($color)
    {
        $this->color = $color;
    }
    
    public function getColor()
    {
        return $this->color;
    }
    
    public function setLevel($level)
    {
        $this->level = $level;
    }
    
    public function getLevel()
    {
        return $this->level;
    }
    
    public function setXp($xp)
    {
        $this->xp = $xp;
    }
    
    public function getXp()
    {
        return $this->xp;
    }

}

?>
