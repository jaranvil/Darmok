<?php

class Address
{
  
    private $m_addressId;
    private $m_address;
    private $m_address2;
    
    public function __construct($in_addressId,$in_address,$in_address2)
    {
        $this->m_addressId = $in_addressId;
        $this->m_address = $in_address;
        $this->m_address2 = $in_address2;
    }
    
    public function getID()
    {
        return ($this->m_addressId);
    }
    
    public function getAddress()
    {
        return ($this->m_address);
    }
    public function getAddress2()
    {
        return ($this->m_address2);
    }
}

?>
